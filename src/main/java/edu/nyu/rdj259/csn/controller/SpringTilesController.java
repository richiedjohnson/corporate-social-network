package edu.nyu.rdj259.csn.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.nyu.rdj259.csn.dao.UserDAO;
import edu.nyu.rdj259.csn.model.Person;
import edu.nyu.rdj259.csn.model.User;

@Controller
public class SpringTilesController {
	
	@Autowired
	private UserDAO userDAO;
	
    @RequestMapping("/index")
    public String index() {
        return "index";
    }
    @RequestMapping("/viewPerson")
    public ModelAndView viewPersons(Model model) {
        Map<String, List<User>> persons =
                new HashMap<String, List<User>>();
        persons.put("persons", userDAO.getAllUsers());
        return new ModelAndView("personList", persons);
    }

}
