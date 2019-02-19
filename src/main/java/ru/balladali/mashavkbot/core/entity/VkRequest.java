package ru.balladali.mashavkbot.core.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class VkRequest {

    @JsonProperty("type")
    private String type;

    @JsonProperty("group_id")
    private Integer groupId;

    @JsonProperty("object")
    private Object object;

}
