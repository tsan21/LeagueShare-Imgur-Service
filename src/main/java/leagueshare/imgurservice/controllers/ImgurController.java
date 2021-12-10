package leagueshare.imgurservice.controllers;

import leagueshare.imgurservice.entities.Imgur;
import leagueshare.imgurservice.repo.ImgurRepo;
import leagueshare.imgurservice.rmq.MessagingConfig;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import okhttp3.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/imgur")
public class ImgurController {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String urlUpload = "https://api.imgur.com/3/upload";
    private final String pathToFile = "src/main/TemporaryFiles/";

    private final ImgurRepo imgurRepo;
    private final MessagingConfig msgConfig;


    @Autowired
    public ImgurController(ImgurRepo imgurRepo, MessagingConfig msgConfig) {
        this.imgurRepo = imgurRepo;
        this.msgConfig = msgConfig;
    }

    @RabbitListener(queues = "user-queue")
    public void receiveMsg(String message){
        get(message);
    }

    @GetMapping("/")
    public ResponseEntity<?> get(String message) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + message);

        Imgur imgur = new Imgur("link1","deletehash1");
        imgurRepo.save(imgur);
        return new ResponseEntity<>(imgur, HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return "this test123";
    }

    @PostMapping("/")
    public ResponseEntity<?> post(@RequestParam(name = "file") MultipartFile file) {
        File fileToPostToImgur = uploadToTemporaryFiles(file);

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("video", file.getName(),
                        RequestBody.create(fileToPostToImgur, MediaType.parse("video/webm")))
                .build();

        Request request = new Request.Builder()
                .url(urlUpload)
                .addHeader("Authorization", "${Imgur_Client_ID}")
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            cleanUp(fileToPostToImgur.toPath());

            String resp = response.body().string();
            JSONObject respData = getDataFromResponse(resp);

            Imgur imgur = new Imgur(respData.getString("link"), respData.getString("deletehash"));
            imgurRepo.save(imgur);
            return new ResponseEntity<>(imgur, HttpStatus.CREATED);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public File uploadToTemporaryFiles(MultipartFile file){
        Path path = Paths.get(pathToFile + file.getOriginalFilename());

        try {
            file.transferTo(path);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        File fileToPostToImgur = new File(path.toString());
        return fileToPostToImgur;
    }

    public JSONObject getDataFromResponse(String respString) throws JSONException {
        JSONObject respJson = new JSONObject(respString);
        JSONObject respData = respJson.getJSONObject("data");
        return respData;
    }

    public void cleanUp(Path path) throws NoSuchFileException, DirectoryNotEmptyException, IOException {
        Files.delete(path);
    }

}
