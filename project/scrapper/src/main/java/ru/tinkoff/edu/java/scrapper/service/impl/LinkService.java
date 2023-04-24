package ru.tinkoff.edu.java.scrapper.service.impl;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkService;

import java.net.URI;

@Service
public class LinkService implements ILinkService {
    @Override
    public boolean supports(URI link) {
        return false;
    }
}
