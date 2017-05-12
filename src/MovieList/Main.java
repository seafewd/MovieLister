package MovieList;

import javafx.beans.binding.StringBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //save all movie objects in a list
        List<Movie> movies = Movie.readMovies("movies.txt");

        printNumberOfMovies(movies);
        System.out.println("Movies starting with the letter X: " + getStartsWith(movies, "X"));
        System.out.println("Number of movies where director name is missing: " + getNumberOfNoDirectors(movies));
        //System.out.println("Number of movies where directors are also actors: " + getNumberOfMoviesWhereDirectorsAreActors(movies));

        System.out.println("Number of total actor names, counting duplicates: " + getNumberOfActorNames(movies, "dup"));
        System.out.println("Number of total actor names, not counting duplicates: " + getNumberOfActorNames(movies, "dis"));
        //System.out.println("Movie with max actors: " + getMovieWithMaxActors(movies));
        getMovieWithMaxActors(movies);
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
        outerList.stream().forEach(System.out::println);
        //movies.stream().map(Movie::getActors).mapToInt(outerList.size()).count();

        //for (List actorList : outerList) System.out.println(actorList);
        //return maxActors;
    }

}
