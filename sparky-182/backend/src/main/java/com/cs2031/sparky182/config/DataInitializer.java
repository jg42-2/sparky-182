package com.cs2031.sparky182.config;

import com.cs2031.sparky182.album.domain.Album;
import com.cs2031.sparky182.album.infrastructure.AlbumRepository;
import com.cs2031.sparky182.review.domain.Review;
import com.cs2031.sparky182.review.infrastructure.ReviewRepository;
import com.cs2031.sparky182.song.domain.Song;
import com.cs2031.sparky182.song.infrastructure.SongRepository;
import com.cs2031.sparky182.tour.domain.Tour;
import com.cs2031.sparky182.tour.infrastructure.TourRepository;
import com.cs2031.sparky182.user.domain.User;
import com.cs2031.sparky182.user.infrastructure.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Seeds the in-memory database every time the app starts.
 * Albums and song titles are blink-182's real discography (5 songs per album).
 * Durations are approximate. Users, reviews and tour dates are fictional demo data.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final String CLASSIC = "Hoppus · DeLonge · Raynor";
    private static final String TRIO = "Hoppus · DeLonge · Barker";
    private static final String SKIBA = "Hoppus · Skiba · Barker";

    private final AlbumRepository albumRepo;
    private final SongRepository songRepo;
    private final UserRepository userRepo;
    private final ReviewRepository reviewRepo;
    private final TourRepository tourRepo;
    private final PasswordEncoder encoder;

    private final Map<String, Album> albums = new HashMap<>();

    public DataInitializer(AlbumRepository albumRepo, SongRepository songRepo, UserRepository userRepo,
                           ReviewRepository reviewRepo, TourRepository tourRepo, PasswordEncoder encoder) {
        this.albumRepo = albumRepo;
        this.songRepo = songRepo;
        this.userRepo = userRepo;
        this.reviewRepo = reviewRepo;
        this.tourRepo = tourRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        seedAlbums();
        seedUsersAndReviews();
        seedTours();
    }

    private void seedAlbums() {
        seed("Cheshire Cat", 1995, CLASSIC, List.of(
                "Carousel|2:52", "M+M's|2:38", "Wasting Time|2:21", "Strings|2:33", "Does My Breath Smell?|2:36"));

        seed("Dude Ranch", 1997, CLASSIC, List.of(
                "Pathetic|2:28", "Voyeur|2:43", "Dammit|2:45", "Josie|3:20", "Apple Shampoo|2:52"));

        seed("Enema of the State", 1999, TRIO, List.of(
                "Dumpweed|2:23", "Aliens Exist|3:12", "What's My Age Again?|2:28",
                "Adam's Song|4:09", "All the Small Things|2:48"));

        seed("Take Off Your Pants and Jacket", 2001, TRIO, List.of(
                "Anthem Part Two|3:48", "First Date|2:51", "The Rock Show|2:51",
                "Stay Together for the Kids|3:59", "Reckless Abandon|3:06"));

        seed("blink-182", 2003, TRIO, List.of(
                "Feeling This|2:53", "I Miss You|3:47", "Down|3:03", "Always|4:11", "Stockholm Syndrome|2:42"));

        seed("Neighborhoods", 2011, TRIO, List.of(
                "Ghost on the Dance Floor|4:12", "Natives|3:52", "Up All Night|4:04",
                "After Midnight|3:22", "Heart's All Gone|3:05"));

        seed("California", 2016, SKIBA, List.of(
                "Cynical|1:56", "Bored to Death|3:55", "She's Out of Her Mind|2:43",
                "Kings of the Weekend|2:58", "California|3:09"));

        seed("Nine", 2019, SKIBA, List.of(
                "The First Time|3:02", "Happy Days|3:07", "Darkside|2:59",
                "Blame It on My Youth|3:01", "I Really Wish I Hated You|3:24"));

        seed("One More Time...", 2023, TRIO, List.of(
                "Anthem Part 3|2:42", "Dance with Me|2:48", "One More Time|3:20", "Edging|2:44", "Fell in Love|2:44"));
    }

    private void seed(String title, int year, String lineup, List<String> songs) {
        String cover = "https://placehold.co/400x400/111827/f472b6/png?text="
                + URLEncoder.encode(title, StandardCharsets.UTF_8);
        Album album = albumRepo.save(new Album(title, year, cover, lineup));
        albums.put(title, album);
        for (int i = 0; i < songs.size(); i++) {
            String[] parts = songs.get(i).split("\\|");
            songRepo.save(new Song(parts[0], parts[1], i + 1, album));
        }
    }

    private void seedUsersAndReviews() {
        userRepo.save(new User("admin@sparky182.com", encoder.encode("admin123"), "Admin Sparky", "ADMIN"));
        User fan = userRepo.save(new User("fan@sparky182.com", encoder.encode("fan123"), "Fan Demo", "USER"));
        User ana = userRepo.save(new User("ana@sparky182.com", encoder.encode("ana123"), "Ana Pop-Punk", "USER"));

        reviewRepo.save(new Review(fan, albums.get("Enema of the State"), 5, "El disco con el que empecé. Cero canciones de relleno."));
        reviewRepo.save(new Review(ana, albums.get("Enema of the State"), 4, "Clásico total, aunque prefiero el sonido de Dude Ranch."));
        reviewRepo.save(new Review(ana, albums.get("Dude Ranch"), 5, "Crudo y rápido. Dammit en bucle."));
        reviewRepo.save(new Review(fan, albums.get("blink-182"), 5, "El más maduro. I Miss You no envejece."));
        reviewRepo.save(new Review(ana, albums.get("California"), 3, "Tiene buenos momentos, pero se siente largo."));
    }

    private void seedTours() {
        tourRepo.saveAll(List.of(
                new Tour("Lima", "Perú", "Estadio Nacional", LocalDate.of(2027, 3, 6), 1200, 280.0),
                new Tour("Santiago", "Chile", "Movistar Arena", LocalDate.of(2027, 3, 9), 0, 310.0),
                new Tour("Buenos Aires", "Argentina", "Estadio Obras", LocalDate.of(2027, 3, 12), 3, 295.0),
                new Tour("São Paulo", "Brasil", "Allianz Parque", LocalDate.of(2027, 3, 15), 900, 330.0),
                new Tour("Bogotá", "Colombia", "Movistar Arena", LocalDate.of(2027, 3, 19), 650, 260.0),
                new Tour("Ciudad de México", "México", "Palacio de los Deportes", LocalDate.of(2027, 3, 23), 1500, 300.0),
                new Tour("San Diego", "EE. UU.", "Pechanga Arena", LocalDate.of(2027, 4, 2), 400, 350.0),
                new Tour("Los Ángeles", "EE. UU.", "Kia Forum", LocalDate.of(2027, 4, 4), 20, 390.0),
                new Tour("Toronto", "Canadá", "Scotiabank Arena", LocalDate.of(2027, 4, 10), 800, 340.0),
                new Tour("Londres", "Reino Unido", "The O2", LocalDate.of(2027, 5, 1), 1100, 360.0),
                new Tour("Madrid", "España", "WiZink Center", LocalDate.of(2027, 5, 5), 0, 320.0),
                new Tour("Tokio", "Japón", "Makuhari Messe", LocalDate.of(2027, 5, 20), 700, 410.0)
        ));
    }
}
