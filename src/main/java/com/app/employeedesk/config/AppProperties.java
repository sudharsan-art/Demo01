package com.app.employeedesk.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppProperties {
    @Value("${app.week.days}")
    private double weekDays;
    @Value("${app.days.hours}")
    private double dayHours;
    @Value("${app.week.number}")
    private int weekNumber;
    @Value("${app.topic.duration}")
    private double topicDuration;
    @Value("${app.subtopic.duration}")
    private double subTopicDuration;
}
