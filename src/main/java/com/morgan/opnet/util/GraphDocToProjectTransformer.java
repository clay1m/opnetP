package com.morgan.opnet.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import org.springframework.core.convert.converter.Converter;

import com.morgan.opnet.model.Project;
import com.morgan.opnet.model.UserDocument;

public class GraphDocToProjectTransformer implements Converter<UserDocument, Project>{

	private UserDocument source;

	@Override
	public Project convert(UserDocument source) {
		Project project = new Project();
		Scanner sc;
		try {
			sc = new Scanner(new FileReader("D:\\test.txt"));
			while (sc.hasNextLine()){
		        System.out.println(sc.next());
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return project;
	}
	
	
}
