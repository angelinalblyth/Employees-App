package controllers;

import db.DBHelper;
import models.Department;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class DepartmentsController {

    public DepartmentsController() {
        this.setupEndpoints();
    }

    private void setupEndpoints() {


        get("/departments", (req, res) -> {
            HashMap<String, Object> model= new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("template", "templates/departments/index.vtl");
            model.put("departments", departments);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/departments/new", (req, res) ->{
            HashMap<String, Object> model = new HashMap<>();
//            List<Department> departments = DBHelper.getAll(Department.class);
//            model.put("departments", departments);
            model.put("template", "/templates/departments/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/departments", (req, res) ->{
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department departments = DBHelper.find(departmentId, Department.class);

            String title = req.queryParams("title");

            Department department = new Department(title);
            DBHelper.save(department);

            res.redirect("/departments");
            return null;
        }, new VelocityTemplateEngine());

        get("/departments/:id", (req,res) ->{
            String strId = req.params(":id");
            Department department = DBHelper.find(Integer.parseInt(strId), Department.class);
            HashMap<String, Object> model = new HashMap<>();
            model.put("department", department);
            model.put("template", "templates/departments/view.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());
    }
}
