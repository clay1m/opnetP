package com.morgan.opnet.controller;

import java.io.File;

import java.io.IOException;

import java.util.List;

import java.util.Locale;

import javax.servlet.ServletContext;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.validation.Valid;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.util.FileCopyUtils;

import org.springframework.validation.BindingResult;

import org.springframework.validation.FieldError;

import org.springframework.web.bind.WebDataBinder;

import org.springframework.web.bind.annotation.InitBinder;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.context.ServletContextAware;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.morgan.opnet.model.Activity;

import com.morgan.opnet.model.Activity.ActivityType;

import com.morgan.opnet.model.ActivityCompressionCost;

import com.morgan.opnet.model.Project;

import com.morgan.opnet.model.DurationDistribution;

import com.morgan.opnet.model.DurationDistribution.DistributionType;

import com.morgan.opnet.model.FileBucket;

import com.morgan.opnet.model.GeneralizedPrecedenceRelation;

import com.morgan.opnet.model.GeneralizedPrecedenceRelation.GPRType;

import com.morgan.opnet.model.Pizza;

import com.morgan.opnet.model.User;

import com.morgan.opnet.model.UserDocument;

import com.morgan.opnet.service.ActivityCompressionCostService;

import com.morgan.opnet.service.ProjectService;

import com.morgan.opnet.service.ActivityService;

import com.morgan.opnet.service.DurationDistributionService;

import com.morgan.opnet.service.GeneralizedPrecedenceRelationService;

import com.morgan.opnet.service.RunGPRService;

import com.morgan.opnet.service.UserDocumentService;

import com.morgan.opnet.service.UserService;

import com.morgan.opnet.swig.activitynet.ActivityNetwork;

import com.morgan.opnet.util.AN;

import com.morgan.opnet.util.CLP;
import com.morgan.opnet.util.CustomProjectDeserializer;
import com.morgan.opnet.util.FileValidator;

import com.morgan.opnet.util.ProcessUtils;

import com.morgan.opnet.util.StringByReference;

import com.morgan.opnet.util.SystemCommand;

import com.sun.jna.Native;

@Controller

@RequestMapping("/")

public class AppController implements ServletContextAware {

	private static AN INSTANCE = (AN) Native.loadLibrary("activitynetswig", AN.class);

	// private static CLP clpLib = (CLP)Native.loadLibrary("clp", CLP.class);

	// Directory c++ GPR exe uses for input, output, and execution

	public static final String EXE_DIR = "C:\\Cpp_Projects\\GPR\\";

	// Filename c++ GPR exe uses for input data

	public static final String GENERIC_INPUT_FILENAME = "Activity_Net_Input.lgf";

	// Filename c++ GPR exe uses for output data

	public static final String GENERIC_OUTPUT_FILENAME = "GPR_Out_0.txt";

	private ServletContext context;

	@Autowired

	UserService userService;

	@Autowired

	UserDocumentService userDocumentService;

	@Autowired

	RunGPRService runGPRService;

	@Autowired

	ProjectService projectService;

	@Autowired

	ActivityService activityService;

	@Autowired

	GeneralizedPrecedenceRelationService gprService;

	@Autowired

	ActivityCompressionCostService costService;

	@Autowired

	DurationDistributionService durationDistributionService;

	@Autowired

	MessageSource messageSource;

	@Autowired

	FileValidator fileValidator;

	@InitBinder("fileBucket")

	protected void initBinder(WebDataBinder binder) {

		binder.setValidator(fileValidator);

	}

	static Logger log = Logger.getLogger(AppController.class.getName());

	/**
	 * 
	 * This method will list all existing users.
	 * 
	 */

	@RequestMapping(value = { "/", "/list" }, method = RequestMethod.GET)

	public String listUsers(ModelMap model) {

		List<User> users = userService.findAllUsers();

		model.addAttribute("users", users);

		return "userslist";

	}

	/**
	 * 
	 * This method will provide the medium to add a new user.
	 * 
	 */

	@RequestMapping(value = { "/newuser" }, method = RequestMethod.GET)

	public String newUser(ModelMap model) {

		User user = new User();

		model.addAttribute("user", user);

		model.addAttribute("edit", false);

		return "registration";

	}

	/**
	 * 
	 * This method will be called on form submission, handling POST request for
	 * 
	 * saving user in database. It also validates the user input
	 * 
	 */

	@RequestMapping(value = { "/newuser" }, method = RequestMethod.POST)

	public String saveUser(@Valid User user, BindingResult result, ModelMap model) {

		if (result.hasErrors()) {

			return "registration";

		}

		/*
		 * 
		 * Preferred way to achieve uniqueness of field [sso] should be
		 * 
		 * implementing custom @Unique annotation and applying it on field [sso]
		 * 
		 * of Model class [User].
		 * 
		 * 
		 * 
		 * Below mentioned peace of code [if block] is to demonstrate that you
		 * 
		 * can fill custom errors outside the validation framework as well while
		 * 
		 * still using internationalized messages.
		 * 
		 * 
		 * 
		 */

		if (!userService.isUserSSOUnique(user.getId(), user.getSsoId())) {

			FieldError ssoError = new FieldError("user", "ssoId", messageSource.getMessage("non.unique.ssoId",

					new String[] { user.getSsoId() }, Locale.getDefault()));

			result.addError(ssoError);

			return "registration";

		}

		userService.saveUser(user);

		model.addAttribute("user", user);

		model.addAttribute("success",

				"User " + user.getFirstName() + " " + user.getLastName() + " registered successfully");

		// return "success";

		return "registrationsuccess";

	}

	/**
	 * 
	 * This method will provide the medium to update an existing user.
	 * 
	 */

	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.GET)

	public String editUser(@PathVariable String ssoId, ModelMap model) {

		User user = userService.findBySSO(ssoId);

		model.addAttribute("user", user);

		model.addAttribute("edit", true);

		return "registration";

	}

	/**
	 * 
	 * This method will be called on form submission, handling POST request for
	 * 
	 * updating user in database. It also validates the user input
	 * 
	 */

	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.POST)

	public String updateUser(@Valid User user, BindingResult result, ModelMap model, @PathVariable String ssoId) {

		if (result.hasErrors()) {

			return "registration";

		}

		userService.updateUser(user);

		model.addAttribute("success",

				"User " + user.getFirstName() + " " + user.getLastName() + " updated successfully");

		return "registrationsuccess";

	}

	/**
	 * 
	 * This method will delete an user by it's SSOID value.
	 * 
	 */

	@RequestMapping(value = { "/delete-user-{ssoId}" }, method = RequestMethod.GET)

	public String deleteUser(@PathVariable String ssoId) {

		userService.deleteUserBySSO(ssoId);

		return "redirect:/list";

	}

	@RequestMapping(value = { "/add-document-{userId}" }, method = RequestMethod.GET)

	public String addDocuments(@PathVariable int userId, ModelMap model) {

		User user = userService.findById(userId);

		model.addAttribute("user", user);

		FileBucket fileModel = new FileBucket();

		model.addAttribute("fileBucket", fileModel);

		List<UserDocument> documents = userDocumentService.findAllByUserId(userId);

		model.addAttribute("documents", documents);

		return "managedocuments";

	}

	@RequestMapping(value = { "/download-document-{userId}-{docId}" }, method = RequestMethod.GET)

	public String downloadDocument(@PathVariable int userId, @PathVariable int docId, HttpServletResponse response)

			throws IOException {

		UserDocument document = userDocumentService.findById(docId);

		response.setContentType(document.getType());

		response.setContentLength(document.getContent().length);

		response.setHeader("Content-Disposition", "attachment; filename=\"" + document.getName() + "\"");

		FileCopyUtils.copy(document.getContent(), response.getOutputStream());

		return "redirect:/add-document-" + userId;

	}

	@RequestMapping(value = { "/delete-document-{userId}-{docId}" }, method = RequestMethod.GET)

	public String deleteDocument(@PathVariable int userId, @PathVariable int docId) {

		userDocumentService.deleteById(docId);

		return "redirect:/add-document-" + userId;

	}

	@RequestMapping(value = { "/run-document-{userId}-{docId}" }, method = RequestMethod.GET)

	public String runDocument(@PathVariable int userId, @PathVariable int docId) {

		UserDocument document = userDocumentService.findById(docId);

		User user = document.getUser();

		String exeDir = context.getRealPath("/WEB-INF/executables");

		exeDir = exeDir + File.separator;

		String tmpDir = "/tmp/";

		String libDir = "/usr/lib/";

		File newLib = new File(libDir + "libactivitynetswig.so");

		if (!newLib.exists()) {

			try {

				FileCopyUtils.copy(new File(exeDir + "libactivitynetswig.so"), newLib);

			} catch (IOException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

		}

		String inFileName = document.getName();

		String outFileName = inFileName.split("\\.")[0] + ".out";

		File inputFile = new File(tmpDir + inFileName);

		try {

			FileCopyUtils.copy(document.getContent(), inputFile);

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		File outputFile = runGPRService.run(inFileName, outFileName);

		try {

			saveDocument(outputFile, user);

		} catch (IOException e1) {

			// TODO Auto-generated catch block

			e1.printStackTrace();

		}

		return "redirect:/add-document-" + userId;

	}

	@RequestMapping(value = { "/view-document-{userId}-{docId}" }, method = RequestMethod.GET)

	public String viewDocument(@PathVariable int userId, @PathVariable int docId, ModelMap model) {

		// prep for graph view

		User user = userService.findWithProjectsById(userId);

		Project project = new Project("project2");

		user.addProject(project);

		Activity A1 = new Activity("A1", ActivityType.REG, project);

		Activity A2 = new Activity("A2", ActivityType.REG, project);

		Activity A3 = new Activity("A3", ActivityType.REG, project);

		Activity A4 = new Activity("A4", ActivityType.REG, project);

		Activity A5 = new Activity("A5", ActivityType.REG, project);

		Activity A6 = new Activity("A6", ActivityType.REG, project);

		Activity A7 = new Activity("A7", ActivityType.REG, project);

		Activity A8 = new Activity("A8", ActivityType.REG, project);

		Activity A9 = new Activity("A9", ActivityType.REG, project);

		Activity A10 = new Activity("A10", ActivityType.REG, project);

		Activity A11 = new Activity("A11", ActivityType.REG, project);

		DurationDistribution d1 = new DurationDistribution(DistributionType.Single, "8");

		DurationDistribution d2 = new DurationDistribution(DistributionType.Single, "6");

		DurationDistribution d3 = new DurationDistribution(DistributionType.Single, "13");

		DurationDistribution d4 = new DurationDistribution(DistributionType.Single, "10");

		DurationDistribution d5 = new DurationDistribution(DistributionType.Single, "8");

		DurationDistribution d6 = new DurationDistribution(DistributionType.Single, "9");

		DurationDistribution d7 = new DurationDistribution(DistributionType.Single, "9");

		DurationDistribution d8 = new DurationDistribution(DistributionType.Single, "8");

		DurationDistribution d9 = new DurationDistribution(DistributionType.Single, "7");

		DurationDistribution d10 = new DurationDistribution(DistributionType.Single, "4");

		DurationDistribution d11 = new DurationDistribution(DistributionType.Single, "0");

		A1.setDurationDistribution(d1);

		A2.setDurationDistribution(d2);

		A3.setDurationDistribution(d3);

		A4.setDurationDistribution(d4);

		A5.setDurationDistribution(d5);

		A6.setDurationDistribution(d6);

		A7.setDurationDistribution(d7);

		A8.setDurationDistribution(d8);

		A9.setDurationDistribution(d9);

		A10.setDurationDistribution(d10);

		A11.setDurationDistribution(d11);

		GeneralizedPrecedenceRelation SS1_2 = new GeneralizedPrecedenceRelation("SS1_2", GPRType.SS, 7.0, A1, A2);

		GeneralizedPrecedenceRelation FS2_3 = new GeneralizedPrecedenceRelation("FS2_3", GPRType.FS, 0.0, A2, A3);

		GeneralizedPrecedenceRelation FS2_4 = new GeneralizedPrecedenceRelation("FS2_4", GPRType.FS, 1.0, A2, A4);

		GeneralizedPrecedenceRelation FS2_5 = new GeneralizedPrecedenceRelation("FS2_5", GPRType.FS, 2.0, A2, A5);

		GeneralizedPrecedenceRelation SF3_7 = new GeneralizedPrecedenceRelation("SF3_7", GPRType.SF, 14.0, A3, A7);

		GeneralizedPrecedenceRelation FF3_8 = new GeneralizedPrecedenceRelation("FF3_8", GPRType.FF, 5.0, A3, A8);

		GeneralizedPrecedenceRelation SS4_6 = new GeneralizedPrecedenceRelation("SS4_6", GPRType.SS, 5.0, A4, A6);

		GeneralizedPrecedenceRelation SS4_7 = new GeneralizedPrecedenceRelation("SS4_7", GPRType.SS, 7.0, A4, A7);

		GeneralizedPrecedenceRelation SS5_6 = new GeneralizedPrecedenceRelation("SS5_6", GPRType.SS, 7.0, A5, A6);

		GeneralizedPrecedenceRelation SS6_10 = new GeneralizedPrecedenceRelation("SS6_10", GPRType.SS, 7.0, A6, A10);

		GeneralizedPrecedenceRelation FF7_9 = new GeneralizedPrecedenceRelation("FF7_9", GPRType.FF, 7.0, A7, A9);

		GeneralizedPrecedenceRelation SS8_9 = new GeneralizedPrecedenceRelation("SS8_9", GPRType.SS, 6.0, A8, A9);

		GeneralizedPrecedenceRelation SF9_10 = new GeneralizedPrecedenceRelation("SF9_10", GPRType.SF, 7.0, A9, A10);

		GeneralizedPrecedenceRelation FS9_11 = new GeneralizedPrecedenceRelation("FS9_11", GPRType.FS, 0.0, A9, A11);

		GeneralizedPrecedenceRelation FS10_11 = new GeneralizedPrecedenceRelation("FS10_11", GPRType.FS, 0.0, A10, A11);

		GeneralizedPrecedenceRelation[] A1Succ = { SS1_2 };

		GeneralizedPrecedenceRelation[] A2Succ = { FS2_3, FS2_4, FS2_5 };

		GeneralizedPrecedenceRelation[] A3Succ = { SF3_7, FF3_8 };

		GeneralizedPrecedenceRelation[] A4Succ = { SS4_6, SS4_7 };

		GeneralizedPrecedenceRelation[] A5Succ = { SS5_6 };

		GeneralizedPrecedenceRelation[] A6Succ = { SS6_10 };

		GeneralizedPrecedenceRelation[] A7Succ = { FF7_9 };

		GeneralizedPrecedenceRelation[] A8Succ = { SS8_9 };

		GeneralizedPrecedenceRelation[] A9Succ = { SF9_10, FS9_11 };

		GeneralizedPrecedenceRelation[] A10Succ = { FS10_11 };

		GeneralizedPrecedenceRelation[] A2Pred = { SS1_2 };

		GeneralizedPrecedenceRelation[] A3Pred = { FS2_3 };

		GeneralizedPrecedenceRelation[] A4Pred = { FS2_4 };

		GeneralizedPrecedenceRelation[] A5Pred = { FS2_5 };

		GeneralizedPrecedenceRelation[] A6Pred = { SS4_6, SS5_6 };

		GeneralizedPrecedenceRelation[] A7Pred = { SF3_7, SS4_7 };

		GeneralizedPrecedenceRelation[] A8Pred = { FF3_8 };

		GeneralizedPrecedenceRelation[] A9Pred = { FF7_9, SS8_9 };

		GeneralizedPrecedenceRelation[] A10Pred = { SS6_10, SF9_10 };

		GeneralizedPrecedenceRelation[] A11Pred = { FS9_11, FS10_11 };

		A1.setGprSuccessorsWithArray(A1Succ);

		A2.setGprSuccessorsWithArray(A2Succ);

		A3.setGprSuccessorsWithArray(A3Succ);

		A4.setGprSuccessorsWithArray(A4Succ);

		A5.setGprSuccessorsWithArray(A5Succ);

		A6.setGprSuccessorsWithArray(A6Succ);

		A7.setGprSuccessorsWithArray(A7Succ);

		A8.setGprSuccessorsWithArray(A8Succ);

		A9.setGprSuccessorsWithArray(A9Succ);

		A10.setGprSuccessorsWithArray(A10Succ);

		A2.addGprPredecessor(SS1_2);

		// A2.setGprPredecessors(A2Pred);

		A3.setGprPredecessorsWithArray(A3Pred);

		A4.setGprPredecessorsWithArray(A4Pred);

		A5.setGprPredecessorsWithArray(A5Pred);

		A6.setGprPredecessorsWithArray(A6Pred);

		A7.setGprPredecessorsWithArray(A7Pred);

		A8.setGprPredecessorsWithArray(A8Pred);

		A9.setGprPredecessorsWithArray(A9Pred);

		A10.setGprPredecessorsWithArray(A10Pred);

		A11.setGprPredecessorsWithArray(A11Pred);

		Activity[] activities = { A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11 };

		project.setActivitiesWithArray(activities);

		for (Activity a : project.getActivities()) {

			a.addCompressionCost(new ActivityCompressionCost(2.0, 4.0, 20.0));

			a.addCompressionCost(new ActivityCompressionCost(4.0, 8.0, 10.0));

			a.addCompressionCost(new ActivityCompressionCost(8.0, 12.0, 5.0));

		}

		userService.updateUser(user);

		ObjectMapper objectMapper = new ObjectMapper();
		String projectAsJson = null;

		try {
			projectAsJson = objectMapper.writeValueAsString(project);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		model.addAttribute("project", projectAsJson);
		model.addAttribute("userId", user.getId());
		model.addAttribute("projectId", project.getProjectId());

		// return project;

		return "index";

	}


	@RequestMapping(value = "/update-project-{userId}-{projectId}", method = RequestMethod.POST)
	public @ResponseBody String updateProject(@RequestParam String proj, ModelMap model, @PathVariable int userId,
			@PathVariable int projectId) {

		User user = userService.findById(userId);

		model.addAttribute("user", user);

		System.out.println(proj);
		
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Project.class, new CustomProjectDeserializer());
		mapper.registerModule(module);
		 
		Project project = null;
		try {
			project = mapper.readValue(proj, Project.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		project.setUser(user);
		
		projectService.updateProject(project);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String projectAsJson = null;

		try {
			projectAsJson = objectMapper.writeValueAsString(project);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		model.addAttribute("project", projectAsJson);
		model.addAttribute("userId", user.getId());
		model.addAttribute("projectId", project.getProjectId());

		return "redirect:/view-document-" + userId;

	}

	// @RequestMapping(value = {"/update-project-{userId}-{projectId}" }, method
	// = RequestMethod.POST,consumes="application/json",headers =
	// "content-type=application/x-www-form-urlencoded")

	// public @ResponseBody String updateProject(HttpServletRequest request,
	// ModelMap model, @PathVariable int userId, @PathVariable int projectId) {

	//

	// User user = userService.findById(userId);

	// model.addAttribute("user", user);

	//

	// String projectAsJSON = request.getParameter("project");

	//

	//

	//

	// project.setUser(user);

	// project.setProjectId(projectId);

	// projectService.saveProject(project);

	//

	// return "redirect:/view-document-" + userId;

	//

	// }

	@RequestMapping(value = "/pizzavalley/{pizzaName}", method = RequestMethod.GET)

	public String getPizza(@PathVariable String pizzaName, ModelMap model) {

		Pizza pizza = new Pizza(pizzaName);

		model.addAttribute("pizza", pizza);

		return "pizza";

	}

	@RequestMapping(value = { "/add-document-{userId}" }, method = RequestMethod.POST)

	public String uploadDocument(@Valid FileBucket fileBucket, BindingResult result, ModelMap model,

			@PathVariable int userId) throws IOException {

		if (result.hasErrors()) {

			System.out.println("validation errors");

			User user = userService.findById(userId);

			model.addAttribute("user", user);

			List<UserDocument> documents = userDocumentService.findAllByUserId(userId);

			model.addAttribute("documents", documents);

			return "managedocuments";

		} else {

			System.out.println("Fetching file");

			User user = userService.findById(userId);

			model.addAttribute("user", user);

			saveDocument(fileBucket, user);

			return "redirect:/add-document-" + userId;

		}

	}

	private void saveDocument(FileBucket fileBucket, User user) throws IOException {

		UserDocument document = new UserDocument();

		MultipartFile multipartFile = fileBucket.getFile();

		document.setName(multipartFile.getOriginalFilename());

		document.setDescription(fileBucket.getDescription());

		document.setType(multipartFile.getContentType());

		document.setContent(multipartFile.getBytes());

		document.setUser(user);

		userDocumentService.saveDocument(document);

	}

	private void saveDocument(File file, User user) throws IOException {

		UserDocument document = new UserDocument();

		byte[] bFile = new byte[(int) file.length()];

		try {

			bFile = FileCopyUtils.copyToByteArray(file);

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		document.setName(file.getName());

		document.setDescription(user.toString());

		document.setType("text");

		document.setContent(bFile);

		document.setUser(user);

		userDocumentService.saveDocument(document);

	}

	@Override

	public void setServletContext(ServletContext servletContext) {

		this.context = servletContext;

	}

}
