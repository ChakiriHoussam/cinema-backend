package org.sid.cinema.service;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.sid.cinema.dao.CategoieRepository;
import org.sid.cinema.dao.CinemaRepository;
import org.sid.cinema.dao.FilmRepository;

import org.sid.cinema.dao.PlaceRepository;
import org.sid.cinema.dao.ProjectionRepository;
import org.sid.cinema.dao.SalleRepository;
import org.sid.cinema.dao.SeanceRepository;
import org.sid.cinema.dao.TicketRepository;
import org.sid.cinema.dao.VilleRepository;
import org.sid.cinema.entites.Categorie;
import org.sid.cinema.entites.Cinema;
import org.sid.cinema.entites.Film;
import org.sid.cinema.entites.Place;
import org.sid.cinema.entites.Projection;
import org.sid.cinema.entites.Salle;
import org.sid.cinema.entites.Seance;
import org.sid.cinema.entites.Ticket;
import org.sid.cinema.entites.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CinemaInitServiceImp implements IcinemaInitService {
	
	@Autowired
	public VilleRepository villeRepository;
	@Autowired
	public CinemaRepository cinemaRepository;
	@Autowired
	public SalleRepository salleRepository;
	@Autowired
	public PlaceRepository placeRepository;
	@Autowired
	public SeanceRepository seanceRepository;
	@Autowired 
	public FilmRepository filmRepository;
	@Autowired
	public ProjectionRepository projectionRepository;
	@Autowired
	public CategoieRepository categorieRepository;	
	@Autowired
	public TicketRepository ticketRepository;
	
	@Override
	public void initVilles() {
		// TODO Auto-generated method stub
		Stream.of("Casablanca","Marrakech","Rabat","Tanger").forEach(nameVille->{
			Ville ville =new Ville();
			ville.setName(nameVille);
			villeRepository.save(ville);
			
			
		});
		
	}

	@Override
	public void initcinemas() {
		// TODO Auto-generated method stub
		villeRepository.findAll().forEach(v->{
			Stream.of("Megarama","Imax","Founoun","Chahrazad","Rif")
			.forEach(nameCinema->{
				Cinema cinema = new Cinema();
				cinema.setName(nameCinema);
				cinema.setNombreSalles(3+(int)(Math.random()*7));
				cinema.setVille(v);
				cinemaRepository.save(cinema);
			});
		});
		
	}

	@Override
	public void initSalles() {
		// TODO Auto-generated method stub
		cinemaRepository.findAll().forEach(cinema->{
			for (int i = 0; i < cinema.getNombreSalles(); i++) {
				Salle salle =new Salle();
				salle.setName("Salle "+(i+1));
				salle.setCinema(cinema);
				salle.setNombrePlace(15+(int)(Math.random()*20));
				salleRepository.save(salle);
			}
		});
	}

	@Override
	public void initPlaces() {
		// TODO Auto-generated method stub
		salleRepository.findAll().forEach(salle->{
			for (int i = 0; i < salle.getNombrePlace(); i++) {
				Place place= new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
		
	}

	@Override
	public void initSeances() {
		// TODO Auto-generated method stub
		DateFormat dateFormat= new SimpleDateFormat("HH:mm");
		Stream.of("12:00","15:00","17:00","19:00","21:00").forEach(s->{
			Seance seance =new Seance();
			try {
				seance.setHeureDebut(dateFormat.parse(s));
				seanceRepository.save(seance);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
	}

	@Override
	public void initCategories() {
		// TODO Auto-generated method stub
		Stream.of("Histoire","Actions","Fiction","Drame").forEach(cat->{
			Categorie categorie = new Categorie();
			categorie.setName(cat);
			categorieRepository.save(categorie);
			
		});
		
	}

	@Override
	public void initFilm() {
			// TODO Auto-generated method stub
			double[] durees= new double[] {1,1.5,2.5,3};
			List<Categorie> categories = categorieRepository.findAll();
			// TODO Auto-generated method stub
		 Stream.of("the good the bad the ugly","the wolf of the wall street","aviator","Blood Diamond","the revenant","Prison Break","Django unchained","Inception","Shutter Island","the great gatsby")
		 .forEach(titreFilm->{
			Film film =new Film();
			film.setTitre(titreFilm);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(titreFilm.replaceAll(" ","")+".jpg");
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
		 });
	}

	@Override
	public void initProjections() {
		// TODO Auto-generated method stub
		double [] prices = new double [] {30,50,60,70,90,100};
		List<Film> films=filmRepository.findAll();
		
		villeRepository.findAll().forEach(ville->{
			ville.getCinemas().forEach(cinema->{
				cinema.getSalles().forEach(salle->{
					int index = new Random().nextInt(films.size());
					 Film film=films.get(index); 
						  seanceRepository.findAll().forEach(seance->{
							Projection projection = new Projection();
							projection.setDateProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(prices[new Random().nextInt(prices.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionRepository.save(projection);
						});
					});
				});
			});
		
	}

	@Override
	public void initTickets() {
		// TODO Auto-generated method stub

		  projectionRepository.findAll().forEach(p->{
			 p.getSalle().getPlaces().forEach(place->{
				Ticket ticket =new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
		});
		
		 
			
		
	}

}
