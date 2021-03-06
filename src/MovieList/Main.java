package MovieList;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //save all movie objects in a list
        List<Movie> movies = Movie.readMovies("movies.txt");

        printNumberOfMovies(movies);
        System.out.println("---------------------------------");
        System.out.println("Number of movies where director name is missing: " + getNumberOfNoDirectors(movies));
        System.out.println("---------------------------------");
        System.out.println("Movies starting with the letter X: " + getStartsWith(movies, "X"));
        System.out.println("---------------------------------");
        System.out.println("Number of movies where directors are also actors: " + getNumberOfMoviesWhereDirectorsAreActors(movies));
        System.out.println("---------------------------------");
        getMovieWithMaxActors(movies);
        System.out.println("---------------------------------");
        System.out.println("Number of total actor names, counting duplicates: " + getNumberOfActorNames(movies, "dup"));
        System.out.println("Number of total actor names, not counting duplicates: " + getNumberOfActorNames(movies, "dis"));
        System.out.println("---------------------------------");
        getMovieStarting(movies);
        System.out.println("---------------------------------");
        getTop10OccurringTitles(movies);
        System.out.println("---------------------------------");
        getDirectorWithMostMovies(movies);
    }

    public static String findFirstWord(String s) {
        if(s.contains(" ")){
            s = s.substring(0, s.indexOf(" "));
        }
        return s;
    }

    public static void printNumberOfMovies(List<Movie> movies) {
        //print number of movies
        System.out.println("Total number of movies in list: " + movies.stream().count());
    }

    public static List<Movie> getStartsWith(List<Movie> movies, String character) {
        //Get list of movies starting with the letter "X"
        return movies.stream()
                .filter(x -> x.getTitle().startsWith(character))
                .collect(Collectors.toList());
    }

    public static long getNumberOfNoDirectors(List<Movie> movies) {
        //Get number of movies where director name is missing
        return movies.stream()
                .flatMap(x -> x.getDirectors()
                        .stream())
                .filter(x -> x.isEmpty())
                .count();
    }

    public static long getNumberOfActorNames(List<Movie> movies, String filterType) {
        long totalActors = -1;

        //Get number of unique actor names (duplicates)
        if (filterType == "dup") {
            totalActors = movies.stream()
                    .flatMap(x -> x.getActors().stream())
                    .count();
        }

        //Get number of unique actor names (duplicates)
        if (filterType == "dis") {
            totalActors = movies.stream()
                    .flatMap(x -> x.getActors().stream())
                    .distinct()
                    .count();
        }
        return totalActors;
    }

    public static long getNumberOfMoviesWhereDirectorsAreActors(List<Movie> movies){
        //Get number of movies where directors are also actors
        long count = movies.stream()
                .filter(x -> x.getActors()
                        .stream()
                        .anyMatch(x.getDirectors()::contains))
                .count();

        return count;
    }



    public static void getMovieWithMaxActors(List<Movie> movies) {
        long maxActors = 0;

        //find number of actors in the movie with the most amount of actors
        List<List<String>> outerList = movies.stream()
                .map(x -> x.getActors())
                .collect(Collectors.toList());

        for (List innerList : outerList) {
            int innerListLength = innerList.size();
            if (innerListLength > maxActors) {
                maxActors = innerListLength;
            }
        }

        long maxActorsInMovie = maxActors;

        //find the movie title with max number of actors
        List<String> movieList = movies.stream()
                .filter(x -> x.getActors().size() == maxActorsInMovie)
                .map(x -> x.getTitle()).collect(Collectors.toList());

        String movie = movieList.get(0);

        //find the director's name
        List<List<String>> directorsOuter = movies.stream()
                .filter(x -> x.getActors().size() == maxActorsInMovie)
                .map(x -> x.getDirectors())
                .collect(Collectors.toList());

        List<String> directors = directorsOuter.get(0);

        System.out.println("Movie with most amount of actors: " + movie);
        System.out.println("Number of actors: " + maxActorsInMovie);
        System.out.println("Director name(s): " + directors);

    }

    public static void getMovieStarting(List<Movie> movies) {
        List<String> titleList = movies.stream()
                .map(x -> x.getTitle())
                .collect(Collectors.toList());

        Map<String, List<String>> startsWith = titleList.stream()
                .collect(Collectors.groupingBy(x -> x.substring(0, 1)));

        System.out.println("Number of movies starting with the letter/character...");
        for (Map.Entry<String, List<String>> map : startsWith.entrySet()) {
            System.out.print("[" + map.getKey() + "]: ");
            System.out.print(map.getValue().stream().count() + " ");
        }
        System.out.print("\n");
    }

    public static void getTop10OccurringTitles(List<Movie> movies) {
        List<String> titleList = movies.stream()
                .map(x -> x.getTitle())
                .collect(Collectors.toList());

        //list with only the first word
        List<String> firstWords = titleList.stream()
                .map(s -> findFirstWord(s))
                .collect(Collectors.toList());

        //find occurrences of the first word
        Map<String, Long> occurrences = firstWords.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        //sort list by descending, limit to only top 10 and print
        System.out.println("Top 10 movies starting with the word...");
        occurrences.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .forEach(System.out::println);
    }

    public static void getDirectorWithMostMovies(List<Movie> movies) {
        //print name of director who made the most movies & their list of movie titles

        //list of directors (only the first name in director list), empty entries filtered out
        List<String> directorList = movies.stream()
                .map(x -> x.getDirectors().get(0))
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toList());

        //find occurrences of director names
        Map<String, Long> occurrences = directorList.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        //sort the list and save only the director with the most movies
        occurrences = occurrences.entrySet().stream()
                .sorted(Map.Entry.<String, Long> comparingByValue().reversed())
                .limit(1)
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        Map<Long, String> occurrencesInversed = occurrences.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        Map.Entry<String, Long> entry = occurrences.entrySet().iterator().next();

        String director = occurrencesInversed.get(entry.getValue());

        //get list of movies from this director
        List<String> movieTitles = movies.stream()
                .filter(x -> x.getDirectors().get(0).equals(director))
                .map(x -> x.getTitle())
                .collect(Collectors.toList());

        System.out.println("Director who made the largest number of movies: " + director);
        System.out.println("List of director's movies: " + movieTitles);
    }
}