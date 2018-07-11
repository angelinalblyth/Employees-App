package controllers;

import static spark.Spark.get;
import db.Seeds;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;

public class MainController {

    public static void main(String[] args) {

        EmployeesController employeesController = new EmployeesController();
        ManagersController managersController = new ManagersController();
        EngineersController engineersController = new EngineersController();
        DepartmentsController departmentsController = new DepartmentsController();
        Seeds.seedData();

        get("/", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();

            model.put("template", "templates/index.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());
    }
}
