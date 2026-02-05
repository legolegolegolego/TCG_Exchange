package com.es.tcg_exchange.service;

import com.es.tcg_exchange.repository.CartaModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartaModeloService {

    @Autowired
    private CartaModeloRepository cmRepository;
}
