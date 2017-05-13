package MovieList;

import javafx.beans.binding.StringBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //save all movie objects in a list
        List<Movie> movies = Movie.readMovies("movies.txt");

        //printNumberOfMovies(movies);
        //System.out.println("Movies starting with the letter X: " + getStartsWith(movies, "X"));
        //System.out.println("Number of movies where director name is missing: " + getNumberOfNoDirectors(movies));
        //System.out.println("Number of movies where directors are also actors: " + getNumberOfMoviesWhereDirectorsAreActors(movies));

        //System.out.println("Number of total actor names, counting duplicates: " + getNumberOfActorNames(movies, "dup"));
        //System.out.println("Number of total actor names, not counting duplicates: " + getNumberOfActorNames(movies, "dis"));
        //System.out.println("Movie with max actors: " + getMovieWithMaxActors(movies));
        //getMovieStarting(movies);
        //getMovieWithMaxActors(movies);
        //getTop1OccurringTitles(movies);
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
        System.out.println("Total number of movies in list: " + movies.size());

    }

    public static List<Movie> getStartsWith(List<Movie> movies, String character) {
        //Get list of movies starting with the letter "X"
        return movies.stream().filter(x -> x.getTitle().startsWith(character)).collect(Collectors.toList());
    }

    public static long getNumberOfNoDirectors(List<Movie> movies) {
        //Get number of movies where director name is missing
        return movies.stream().flatMap(x -> x.getDirectors().stream()).filter(x -> x.isEmpty()).count();
    }

    public static long getNumberOfActorNames(List<Movie> movies, String filterType) {
        long totalActors = -1;

        //Get number of unique actor names (duplicates)
        if (filterType == "dup") {
            totalActors = movies.stream().flatMap(x -> x.getActors().stream()).count();
        }

        //Get number of unique actor names (duplicates)
        if (filterType == "dis") {
            totalActors = movies.stream().flatMap(x -> x.getActors().stream().distinct()).count();
        }
        return totalActors;
    }

    public static long getNumberOfMoviesWhereDirectorsAreActors(List<Movie> movies){
        //Get number of movies where directors are also actors
        List<String> alLActors = movies.stream().flatMap(x -> x.getDirectors().stream().distinct()).collect(Collectors.toList());
        List<String> allDirectors = movies.stream().flatMap(x -> x.getActors().stream()).distinct().collect(Collectors.toList());

        //only keep the names that exist in both lists
        allDirectors.retainAll(alLActors);
        return allDirectors.stream().count();
    }

    public static void getMovieWithMaxActors(List<Movie> movies) {
        long maxActors = 0;
        List<List<String>> outerList = movies.stream().map(x -> x.getActors()).collect(Collectors.toList());
        /*for (List innerList : outerList) {
            int innerListLength = innerList.size();
            if (innerListLength > maxActors) {
                maxActors = innerListLength;
            }
        }*/
        System.out.println(movies.stream().map(x -> x.getActors()).min(Comparator.comparing(List<String>::size)).get());
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
    }

    public static void getTop1OccurringTitles(List<Movie> movies) {
        List<String> titleList = movies.stream().map(x -> x.getTitle()).collect(Collectors.toList());

        //list with only the first word
        List<String> firstWords = titleList.stream().map(s -> findFirstWord(s)).collect(Collectors.toList());

        //find occurrences of the first word
        Map<String, Long> occurrences = firstWords.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        //sort list by descending, limit to only top 10 and print
        System.out.println("Top 10 movies starting with the word...");
        occurrences.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).limit(10).forEach(System.out::println);
    }

    public static void getDirectorWithMostMovies(List<Movie> movies) {
        //print name of director who made the most movies & their list of movie titles

        //list of movies
        List<String> titleList = movies.stream().map(x -> x.getTitle()).collect(Collectors.toList());

        //list of directors (only the first name in director list), empty entries filtered out
        List<String> directorList = movies.stream().map(x -> x.getDirectors().get(0)).filter(x -> !x.isEmpty()).collect(Collectors.toList());

        //find occurrences of director names
        Map<String, Long> occurrences = directorList.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        //sort the list and save only the director with the most movies
        //FIXME: extract string value from the (only) key in the occurrences map
        occurrences = occurrences.entrySet().stream().sorted(Map.Entry.<String, Long> comparingByValue().reversed()).limit(1).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
        Map<Long, String> occurrencesInversed = occurrences.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        System.out.println("Director who made the largest number of movies: D. W. Griffith, " + occurrences.get("D. W. Griffith"));
        System.out.println("List of director's movies: " /*+ print list*/);

        //map director name with their list of movies
        //Map<String, List<String>> directorWithMovies = titleList.stream().collect(groupingBy(directorName, counting()));
    }
}
