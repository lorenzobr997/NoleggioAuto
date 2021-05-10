package polito.it.noleggio.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;

import polito.it.noleggio.model.Event.EventType;

public class Simulator {
	
	private PriorityQueue<Event> q;
	
	private int NC;
	private Duration T_IN;
	private LocalTime oraApertura = LocalTime.of(8, 0);
	private LocalTime oraChiusura = LocalTime.of(20, 0);
	
	private int nAuto;
	
	private int nClienti;
	private int nClientiInsoddisfatti;
	
	
	public void setNumCars (int NC) {
		this.NC = NC;
	}
	
	public void setClientFrequency(Duration d) {
		this.T_IN = d;
	}
	
	public void run() {
		
		this.q = new PriorityQueue<Event>();
		
		this.nAuto = NC;
		this.nClienti = 0;
		this.nClientiInsoddisfatti = 0; 
	
		LocalTime ora = this.oraApertura;
		while(ora.isBefore(this.oraChiusura)) {
			this.q.add(new Event(ora, EventType.NUOVO_CLIENTE));
			ora = ora.plus(this.T_IN);
		}
		
		while (!this.q.isEmpty()) {
			Event e = this.q.poll();
			processEvent(e);
		}
		
	}
	

	private void processEvent (Event e) {
		switch(e.getType()) {
		case NUOVO_CLIENTE:
			this.nClienti++;
			if(this.nAuto > 0) {
				this.nAuto--;
				double n = Math.random()*3;
				if(n < 1.0) {
					this.q.add(new Event(e.getTime().plus(Duration.of(1, ChronoUnit.HOURS)), EventType.RITORNO_AUTO));
				} else if(n < 2.0) {
					this.q.add(new Event(e.getTime().plus(Duration.of(2, ChronoUnit.HOURS)), EventType.RITORNO_AUTO));
				} else {
					this.q.add(new Event(e.getTime().plus(Duration.of(3, ChronoUnit.HOURS)), EventType.RITORNO_AUTO));
				}
			}else {
				this.nClientiInsoddisfatti ++;
			}
			break;
		case RITORNO_AUTO:
			this.nAuto++;
			break;
		}
	}

	public int getTotClients() {
		return this.nClienti;
	}
	
	public int getDissatisfied(){
		return this.nClientiInsoddisfatti;
	}
}
