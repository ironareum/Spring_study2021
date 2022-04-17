package org.zerock.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;

/**
 * @Author : "IronAreum"
 * @Date : 2022. 1. 25.
 */
@Controller
@Log4j
public class UploadController {
	
	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload form");
	}
	
	
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		
		String uploadFolder = "C:\\upload";
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("-----------------------------");
			log.info("Upload File Name: "+ multipartFile.getOriginalFilename()); //IE의 경우 경로가 표시됨
			log.info("Upload File Size: "+ multipartFile.getSize());
			
			
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			
			try {
				//파일을 저장하는 방법은 간단히 transferTo()를 이용해서 처리가능
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}
	
 
	
	/**
	 * @Author : "IronAreum"
	 * @Date : 2022. 1. 25.
	 * @Method : 첨부파일 ajax 방식으로 등록
	 * @return : void
	 */
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		log.info("upload ajax");
	}
	
	
	/**
	 * @Author : "IronAreum"
	 * @Date : 2022. 1. 25.
	 * @Method : 첨부파일 ajax 방식으로 등록 Action
	 * @return : void
	 */
	@PostMapping("/uploadAjaxAction")
	public void uploadAjaxAction(MultipartFile[] uploadFile) {
		log.info("update ajax post....");
		
		String uploadFolder = "C:\\upload";
		
		//make folder --------
		File uploadPath = new File(uploadFolder, getFolder()); //SimpleDateFormat("yyyy-MM-dd") to (File.seperator)
		log.info("uploadPath: " + uploadPath);
		
		if(uploadPath.exists() == false) {
			uploadPath.mkdirs(); //mkdirs()는 필요시 부모 디렉토리까지 생성 
		}
		//make yyyy/MM/dd folder
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("--------------------");
			log.info("Upload File Name: "+ multipartFile.getOriginalFilename());
			log.info("Upload File Size: "+ multipartFile.getSize());
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			//IE has file path
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
			log.info("only file name: "+ uploadFileName);
			
			//중복 방지를 위한 UUID 사용
			UUID uuid = UUID.randomUUID();  
			uploadFileName = uuid.toString() + "_" + uploadFileName; //동일한 파일명 업로드 시에도 앞에 UUID를 붙여 덮어쓰기 방지
			
			//File saveFile = new File(uploadFolder, uploadFileName);
			File saveFile = new File(uploadPath, uploadFileName);
			
			try {
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			
		}// end for 
		
	}
	
	/**
	 * @Author : "IronAreum"
	 * @Date : 2022. 1. 25.
	 * @Method : 년/월/일 폴더의 생성
	 * @return : String
	 */
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		
		String str = sdf.format(date);
		return str.replace("-", File.separator);
	}
}
