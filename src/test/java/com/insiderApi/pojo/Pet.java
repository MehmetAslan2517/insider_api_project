package com.insiderApi.pojo;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Getter
@Setter
@ToString

public class Pet {

    private long id;
    private Map<String, Object> category;
    private String name;
    private List<String> photoUrls;
    private List<Map<String, Object>> tags;
    private String status;

}
