package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.linkparser.LinkParser;

import java.net.URI;

@RequiredArgsConstructor
@Service
public class LinkService {

    private final LinkParser linkParser;

    public boolean supports(URI link) {
        return linkParser.supports(link);
    }

}
