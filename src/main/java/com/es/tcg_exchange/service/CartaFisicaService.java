package com.es.tcg_exchange.service;

import com.es.tcg_exchange.repository.CartaFisicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartaFisicaService {

    @Autowired
    private CartaFisicaRepository cfRepository;
}
