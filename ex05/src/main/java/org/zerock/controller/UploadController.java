package org.zerock.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

/**
 * @Author : "IronAreum"
 * @Date : 2022. 1. 25.
 */
/**
 * @Author : "IronAreum"
 * @Date : 2022. 4. 17.
 */
/**
 * @Author : "IronAreum"
 * @Date : 2022. 4. 17.
 */
/**
 * @Author : "IronAreum"
 * @Date : 2022. 4. 17.
 */
/**
 * @Author : "IronAreum"
 * @Date : 2022. 4. 17.
 */
@Controller
@Log4j
public class UploadController {

	@GetMapping("/uploadForm")
	public void uploadForm() throws IOException {
		log.info("upload form");

		// 파일생성 방법 출처: https://hianna.tistory.com/588 [어제 오늘 내일]
		File file = new File("c:\\upload\\test1", "test1.txt"); // 디렉토리, 파일명 (아직 생성 안됨). 현재는 파일인스턴스가 생성된 상태
		log.info("File: " + file);

		// createNewFile 이나 OutputStream을 생성해야 파일생성이 됨 & 해당 파일경로가 이미 생성되어 있어야됨
		try {
			if (file.createNewFile()) {
				System.out.println("File created");
			} else {
				System.out.println("File already exists");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// file.mkdirs();

		File opfile = new File("c:\\upload\\test1\\outputStream1.txt");

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(opfile, true); // false: 덮어쓰기
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// java.nio.file.Files
		// Files.createFile()
	}

	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {

		String uploadFolder = "C:\\upload";

		for (MultipartFile multipartFile : uploadFile) {
			log.info("-----------------------------");
			log.info("Upload File Name: " + multipartFile.getOriginalFilename()); // IE의 경우 경로가 표시됨
			log.info("Upload File Size: " + multipartFile.getSize());

			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());

			try {
				// 파일을 저장하는 방법은 간단히 transferTo()를 이용해서 처리가능
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
	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachDTO>> uploadAjaxAction(MultipartFile[] uploadFile) {
		log.info("update ajax post....");
		List<AttachDTO> list = new ArrayList<>();

		String uploadFolder = "C:\\upload";
		String uploadFolderPath = getFolder(); //저장할 폴더 경로생성

		// make folder --------
		File uploadPath = new File(uploadFolder, uploadFolderPath); // uploadFolderPath: SimpleDateFormat("yyyy-MM-dd")																	
		log.info("uploadPath: " + uploadPath);
		if (uploadPath.exists() == false) {
			uploadPath.mkdirs(); // mkdirs()는 필요시 부모 디렉토리까지 생성. make yyyy/MM/dd folder
		}		
		for (MultipartFile multipartFile : uploadFile) {
			log.info("--------------------");
			log.info("Upload File Name: " + multipartFile.getOriginalFilename());
			log.info("Upload File Size: " + multipartFile.getSize());

			AttachDTO attachDTO = new AttachDTO();
			String uploadFileName = multipartFile.getOriginalFilename();
			// IE has file path
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1); //file 이름만 추출
			log.info("only file name: " + uploadFileName);			
			attachDTO.setFileName(uploadFileName);

			// 중복 방지를 위한 UUID 사용
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName; // 동일한 파일명 업로드 시에도 앞에 UUID를 붙여 덮어쓰기 방지

			try {
				// File saveFile = new File(uploadFolder, uploadFileName);
				File saveFile = new File(uploadPath, uploadFileName);
				// 파일 저장
				multipartFile.transferTo(saveFile);

				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);

				// 이미지 타입 확인
				if (checkImageType(saveFile)) {
					attachDTO.setImage(true);
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);

					thumbnail.close();
				}

				list.add(attachDTO);

			} catch (Exception e) {
				log.error(e.getMessage());
			}
		} // end for

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){ 
		log.info("fileName: " + fileName);
	  
		File file = new File("c:\\upload\\" + fileName);	  
		log.info("file: "+ file);
	  
		ResponseEntity<byte[]> result = null;
	  
		try {
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", Files.probeContentType(file.toPath())); //MIME타입 데이터를 헤더 메시지에 포함 (image/png, image/jpg 등) 
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return result;
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

	/**
	 * @Author : "IronAreum"
	 * @Date : 2022. 4. 17.
	 * @Method : 이미지 파일 판단
	 * @return : boolean
	 */
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath()); //타입 확인 
			System.out.println("file.toPath() : " + file.toPath());
			System.out.println("contentType : " + contentType);
			return contentType.startsWith("image");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
