import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Voto {
    private int id;
    private int votanteId;
    private int candidatoId;
    private String timeStamp;

    public Voto(int id, int votanteId, int candidatoId) {
        this.id = id;
        this.votanteId = votanteId;
        this.candidatoId = candidatoId;
        this.timeStamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")); }

    public int getId() { 
     return id; 
   }
    public int getVotanteId()
 {
     return votanteId;
 }
    public int getCandidatoId() 
{ 
      return candidatoId;
 }
    public String getTimeStamp() {
      return timeStamp; 
}

public void setId(int id) { 
this.id = id; 
}
public void setVotanteId(int votanteId) {
this.votanteId = votanteId; 
}
    public void setCandidatoId(int candidatoId) { 
this.candidatoId = candidatoId; 
}
    public void setTimeStamp(String timeStamp) { 
this.timeStamp = timeStamp;
   }
}

class Candidato {
    private int id;
    private String nombre;
    private String partido;
    private Queue<Voto> votosRecibidos;

    public Candidato(int id, String nombre, String partido) {
        this.id = id;
        this.nombre = nombre;
        this.partido = partido;
        this.votosRecibidos = new LinkedList<>();}

    public int getId(){
        return id;
     }
    public String getNombre(){
        return nombre;
}
    public String getPartido() {
        return partido;
        
    }
    public Queue<Voto> getVotosRecibidos(){
        return votosRecibidos;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setPartido(String partido) {
        this.partido = partido; 
    }
    public void agregarVoto(Voto v) {
        votosRecibidos.offer(v);
        }
    public Voto removerVoto(int idVoto) {
        for (Voto v : votosRecibidos) {
            if (v.getId() == idVoto) {
                votosRecibidos.remove(v);
                return v;
            }
        }
        return null;
    }
}
class Votante {

    private int id;
    private String nombre;
    private boolean yaVoto;

    public Votante(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.yaVoto = false;
        }

    public int getId() {
        return id;
    }
    public String getNombre() { 
        return nombre;
        }
    public boolean getYaVoto() { 
        return yaVoto;
        }
    public void setId(int id) { 
        this.id = id;
        }
    public void setNombre(String nombre) { 
        this.nombre = nombre;
        }
    public void setYaVoto(boolean yaVoto) { 
        this.yaVoto = yaVoto;
        }
    public void marcarComoVotado() { 
        this.yaVoto = true;
        }
}

class UrnaElectoral {
    private LinkedList<Candidato> listaCandidatos;
    private Stack<Voto> historialVotos;
    private Queue<Voto> votosReportados;
    private int idCounter;
    private Map<Integer, Votante> votantes;

    public UrnaElectoral() {
        this.listaCandidatos = new LinkedList<>();
        this.historialVotos = new Stack<>();
        this.votosReportados = new LinkedList<>();
        this.idCounter = 1;
        this.votantes = new HashMap<>();}

    public void agregarCandidato(Candidato candidato) {
        listaCandidatos.add(candidato);}

    public void agregarVotante(Votante votante) {
        votantes.put(votante.getId(), votante);}

    public boolean verificarVotante(Votante votante) {
        return !votante.getYaVoto();}

    public boolean registrarVoto(Votante votante, int candidatoId) {
        if (!verificarVotante(votante)) {
            System.out.println("El votante " + votante.getNombre() + " ya ha votado.");
            return false;}

        Candidato candidato = listaCandidatos.stream().filter(c -> c.getId() == candidatoId).findFirst().orElse(null);

        if (candidato == null) {
            System.out.println("Candidato con ID " + candidatoId + "no existe.");
            return false;}

        Voto voto = new Voto(idCounter++, votante.getId(), candidatoId);

        candidato.agregarVoto(voto);
        historialVotos.push(voto);
        votante.marcarComoVotado();

        System.out.println("Voto registrado correctamente. ID del voto: " + voto.getId());
        return true;}

    public boolean reportarVoto(Candidato candidato, int idVoto) {
        Voto voto = candidato.removerVoto(idVoto);

        if (voto == null) {
            System.out.println("Voto con ID " + idVoto + " no encontrado en la cola del candidato.");
            return false;}

        votosReportados.add(voto);
        System.out.println("Voto con ID " + idVoto + " reportado y movido a la cola de votos reportados.");
        return true;}

    public String obtenerResultados() {
        StringBuilder resultados = new StringBuilder();
        resultados.append("Resultados de la elección:");

        for (Candidato candidato : listaCandidatos) {
resultados.append(candidato.getNombre()).append(" (").append(candidato.getPartido()).append("): ").append(candidato.getVotosRecibidos().size()).append(" votos");

}

        resultados.append("Total de votos emitidos: ").append(historialVotos.size());
        resultados.append("Votos reportados: ").append(votosReportados.size());
        return resultados.toString();}
}

public class Main {
    public static void main(String[] args) {

        UrnaElectoral urna = new UrnaElectoral();

        Candidato candidato1 = new Candidato(1, "Juan", "Republicano");
        Candidato candidato2 = new Candidato(2, "Isidora", "Comunista");
        Candidato candidato3 = new Candidato(3, "Javiera", "Socialista");

        urna.agregarCandidato(candidato1);
        urna.agregarCandidato(candidato2);
        urna.agregarCandidato(candidato3);

        Votante votante1 = new Votante(101, "Benjamin");
        Votante votante2 = new Votante(102, "Josefa");
        Votante votante3 = new Votante(103, "Martina");
        Votante votante4 = new Votante(104, "Javier");
        Votante votante5 = new Votante(105, "Sofia");

        urna.agregarVotante(votante1);
        urna.agregarVotante(votante2);
        urna.agregarVotante(votante3);
        urna.agregarVotante(votante4);
        urna.agregarVotante(votante5);

        urna.registrarVoto(votante1, 1);
        urna.registrarVoto(votante2, 2); 
        urna.registrarVoto(votante3, 1);
        urna.registrarVoto(votante4, 3);
        urna.registrarVoto(votante5, 2); 
        urna.registrarVoto(votante1, 2);
        
        if (candidato1.getVotosRecibidos().peek() != null) {
            urna.reportarVoto(candidato1, candidato1.getVotosRecibidos().peek().getId());}
        System.out.println(" " + urna.obtenerResultados());}
}
