package application;

import java.io.*;
import java.util.ArrayList;

public class FileHandler implements IHandler {
	private FileWriter fw;
	private OutputStreamWriter fwO;
	private FileReader fr;
	private InputStreamReader frI;
	private BufferedReader br;
	private BufferedReader brr;
	private File f;
	private ArrayList<Movie> movies;

	public FileHandler(File f) {
		movies = new ArrayList<>();
		if (f == null) {
			throw new NullPointerException();
		}
		this.f = f;
		try {
			fr = new FileReader(f);
			frI= new InputStreamReader(
					new FileInputStream(f.getPath()), "UTF-8");
			br = new BufferedReader(fr);
			brr = new BufferedReader(frI);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// read all movies and save them in arraylist movies
		loadMovies();
	}

	private void loadMovies() {
		String movie = null;

		try {
			brr.readLine();
			while ((movie = brr.readLine()) != null) {
				System.out.println(movie);
				String[] data = movie.split(";");
				movies.add(new Movie(data[0], data[1], data[2], data[3], data[4], data[5], data[6], Integer.parseInt(data[7]),data[8]));
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Movie> selectAllMovies() {
		return movies;
	}

	@Override
	public String addMovie(Movie m) {

		if (movies.add(new Movie(m.getName(), m.getDirector(), m.getCountry(),m.getActors(), m.getLanguage(), Integer.toString(m.getYear()),Integer.toString(m.getDuration()),
				 m.getSeen(), Float.toString(m.getRating()))))
			return new String("Фильм добавлен.");
		else
			return new String("Ошибка.");
	}

	@Override
	public void editMovie(Movie newMovie, Movie selectedMovie) {
		for (Movie m : movies) {
			if (m.getName().equals(selectedMovie.getName()) && m.getYear().equals(selectedMovie.getYear())) {
				m.setName(newMovie.getName());
				m.setDirector(newMovie.getDirector());
				m.setCountry(newMovie.getCountry());
				m.setActors(newMovie.getActors());
				m.setLanguage(newMovie.getLanguage());
				m.setYear(newMovie.getYear());
				m.setDuration(newMovie.getDuration());
				m.setSeen(newMovie.getSeen());
				m.setRating(newMovie.getRating());
			}
		}
	}

	@Override
	public void delMovie(Movie m) {
		Movie toRemove = null;

		for (Movie mov : movies) {
			if (mov.getName().equals(m.getName()) && mov.getYear().equals(m.getYear())) {
				toRemove = mov;
				break;
			}
		}

		movies.remove(toRemove);
	}

	@Override
	public ArrayList<Movie> filterMovies(Movie filter) {
		// TODO - check opciju koji se fieldovi gledaju
		ArrayList<Movie> filteredMovies = new ArrayList<>();
		String tempFilterName = filter.getName();
		String tempFilterDir = filter.getDirector();
		int tempFilterYear = filter.getYear();
		int tempFilterDur = filter.getDuration();
		String tempFilterCountry = filter.getCountry();
		String tempFilterActors = filter.getActors();
		String tempFilterLanguage = filter.getLanguage();
		Float tempFilterRating =  filter.getRating();
		int tempFilterSeen = filter.getSeen();

		if (tempFilterDir.equals("")) // because an empty string occurs in every
										// string
			tempFilterDir = "zzzz";
		if (tempFilterName.equals("")) // because an empty string occurs in
										// every string
			tempFilterName = "zzzz";

		for (Movie m : movies) {
			if (m.getName().contains(tempFilterName) || m.getDirector().contains(tempFilterDir)
					|| m.getYear().equals(tempFilterYear) || m.getDuration().equals(tempFilterDur)
					|| m.getCountry().contains(tempFilterCountry)  || m.getActors().contains(tempFilterActors)
			|| m.getLanguage().contains(tempFilterLanguage)  || m.getRating().equals(tempFilterRating)
			|| m.getSeen() == tempFilterSeen)

				filteredMovies.add(m);
		}

		return filteredMovies;
	}

	@Override
	public void markSeen(Movie m) {
		for (Movie mov : movies) {
			if (m.getName().equals(mov.getName()) && m.getYear().equals(mov.getYear())) {
				mov.setSeen(1);
				break;
			}
		}

	}

	@Override
	public void markUnseen(Movie m) {
		for (Movie mov : movies) {
			if (m.getName().equals(mov.getName()) && m.getYear().equals(mov.getYear())) {
				mov.setSeen(0);
				break;
			}
		}

	}

	@Override
	public ArrayList<Movie> selectSeenMovies() {
		ArrayList<Movie> moviesSeen = new ArrayList<>();

		for (Movie m : movies)
			if (m.getSeen() == 1)
				moviesSeen.add(m);

		return moviesSeen;
	}

	@Override
	public ArrayList<Movie> selectUnseenMovies() {
		ArrayList<Movie> moviesUnseen = new ArrayList<>();

		for (Movie m : movies)
			if (m.getSeen() == 0)
				moviesUnseen.add(m);

		return moviesUnseen;
	}

	@Override
	public void closeConn() {

		try {
			fwO= new OutputStreamWriter(
					new FileOutputStream(f.getPath()), "UTF-8");
			fwO.write("НАЗВАНИЕ;РЕЖИССЕР;СТРАНА;АКТЕРЫ;ЯЗЫК;ГОД;ВРЕМЯ;По книге;Рейтинг;" + System.lineSeparator());
			for (Movie m : movies) {
				fwO.write(m.toString());
			}
			fwO.close();
			fr.close();
			br.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
