package controllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import db.DBHelper;
import models.Department;
import models.Engineer;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class EngineersController {

    public EngineersController() {
        this.setupEndpoints();
    }

    private void setupEndpoints() {

        get("/engineers", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Engineer> engineers = DBHelper.getAll(Engineer.class);
            model.put("template", "templates/engineers/index.vtl");
            model.put("engineers", engineers);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());


        get("/engineers/new", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);
            model.put("template", "templates/engineers/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/engineers", (req, res) -> {
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);

            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int salary = Integer.parseInt(req.queryParams("salary"));

            Engineer newEngineer = new Engineer(firstName, lastName, salary, department);
            DBHelper.save(newEngineer);


            res.redirect("/engineers");
            return null;
        });

        get("/engineers/:id", (req,res) ->{
            String strId = req.params(":id");
            Engineer engineers = DBHelper.find(Integer.parseInt(strId), Engineer.class);
            HashMap<String, Object> model = new HashMap<>();
            model.put("engineer", engineers);
            model.put("template", "templates/engineers/view.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/engineers/:id/delete", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Engineer engineer = DBHelper.find(id, Engineer.class);
            DBHelper.delete(engineer);
            res.redirect("/engineers");
            return null;
        }, new VelocityTemplateEngine());

        get("/engineers/:id/edit", (req, res) ->{
            String strId = req.params(":id");
            Engineer engineer = DBHelper.find(Integer.parseInt(strId), Engineer.class);
            List<Department> departments = DBHelper.getAll(Department.class);
            HashMap<String, Object> model = new HashMap<>();
            model.put("engineer", engineer);
            model.put("departments", departments);
            model.put("template", "templates/engineers/edit.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/engineers/:id", (req, res) ->{


            int managerId = Integer.parseInt(req.params("id"));
            Engineer engineer = DBHelper.find(managerId, Engineer.class);

            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);


            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int salary = Integer.parseInt(req.queryParams("salary"));
            double budget = Double.parseDouble(req.queryParams("budget"));

            engineer.setFirstName(firstName);
            engineer.setLastName(lastName);
            engineer.setSalary(salary);
            engineer.setDepartment(department);

            DBHelper.save(engineer);

            res.redirect("/engineers");
            return null;
        }, new VelocityTemplateEngine());
    }
}
