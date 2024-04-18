package com.backend.api.controllers;

import com.backend.api.models.*;
import com.backend.api.models.exceptions.TmdbServerOffException;
import com.backend.api.models.tmdb.MovieResult;
import com.backend.api.services.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

//retorna dados da lista de filmes e adiciona edita e deleta filmes dela
@RestController
public class WatchlistController {
    @Autowired
    private WatchlistService watchlistService;

    private MovieCustomDetails mapInsertMovie(InsertMovie insertMovie){
        return new MovieCustomDetails(
            insertMovie.originalTitle,
            insertMovie.type,
            insertMovie.author,
            insertMovie.status,
            insertMovie.conclusionDate,
            insertMovie.rating,
            insertMovie.originalTitle
        );
    }

    // pegar toda lista
    @GetMapping("/watchlist")
    public ResponseEntity<Object> getList(){
        ArrayList<WatchlistData> list = watchlistService.getWatchlist();
        return ResponseEntity.status(200).body(list);
    }

    // pegar toda lista filtrada: nome, autor, tipo, status, data concluido/assistido, avaliacao, titulo original
//    @GetMapping("/getlistfilter")
//    public ResponseEntity<Object> getListFilter(@RequestParam Movie movie) {
//        return ResponseEntity.status(200).body(watchListMovies);
//    }

    // pegar filme unico da lista
//    @GetMapping("{movie-id}")
//    public ResponseEntity<Object> GetMovie(@PathVariable("movie-id") int movieId) {
//        return ResponseEntity.status(200).body(movieId);
//    }

    @PostMapping("/watchlist")
    public ResponseEntity<Object> addMovie(@RequestParam Integer tmdbId, @RequestBody InsertMovie insertMovie) {
        MovieCustomDetails movie = mapInsertMovie(insertMovie);
        try{
            Integer result = watchlistService.addMovie(tmdbId, movie);
            return ResponseEntity.status(201).body(new ReturnMovie(result));
        } catch (TmdbServerOffException e){
            return ResponseEntity.status(503).body(new ErrorMessage(e.message));
        }
    }

    // editar filme
    @PutMapping("/watchlist")
    public ResponseEntity<Object> editMovie(@RequestParam Integer id, @RequestParam Integer tmdbId, @RequestBody InsertMovie insertMovie) {
        MovieCustomDetails movie = mapInsertMovie(insertMovie);
        try{
            Integer result = watchlistService.editMovie(id, tmdbId, movie);
            return ResponseEntity.status(200).body(new ReturnMovie(result));
        } catch (TmdbServerOffException e){
            return ResponseEntity.status(503).body(new ErrorMessage(e.message));
        }
    }

    @DeleteMapping("/watchlist")
    public ResponseEntity<Object> RemoveMovie(@RequestParam("id") int id) {
        Integer result = watchlistService.removeMovie(id);
        return ResponseEntity.status(200).body(new ReturnMovie(result));
    }
}