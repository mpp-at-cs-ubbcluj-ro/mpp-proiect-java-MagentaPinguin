package start;

import model.Trial;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import repository.services.ServiceException;

import java.util.concurrent.Callable;

public class Client {
    public static final String URL = "http://localhost:8080/trials";
    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Trial[] getAll() throws RestClientException {
        return execute(() -> restTemplate.getForObject(URL, Trial[].class));
    }

    public Trial getOne(int id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Trial.class));

    }

    public Trial getOneSpecific(Trial trial) {
        return execute(() -> restTemplate.postForObject( String.format("%s/%s", URL, "specific"),trial,Trial.class));

    }

    public Trial addTrial(Trial trial) {
        return execute(() ->
        restTemplate.postForObject(URL,trial,Trial.class));

    }


    public String update(Trial item) {
        return execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, item.getId()), item);
            return "Update la: "+item.getId();
        });
    }

    public String delete(long id) {
        return execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return "S-a sters: "+id;
        });
    }

}
