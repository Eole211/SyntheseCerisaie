package metier;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement(name = "Etudiant")
public class Etudiant {
 
  private String nom;
  private  String prenom;
  private  Date dnaissance;

  public Etudiant() {
  }
public Etudiant(String nom, String prenom, Date dnaissance) {
	super();
	this.nom = nom;
	this.prenom = prenom;
	this.dnaissance = dnaissance;
}

public void setDnaissance(Date dnaissance) {
	this.dnaissance = dnaissance;
}

public void setNom(String nom) {
	this.nom = nom;
}

public void setPrenom(String prenom) {
	this.prenom = prenom;
}

@XmlElement
public String getNom() {
return nom;
}
@XmlElement
public String getPrenom() {
	return prenom;
}
@XmlElement(name = "DatenNaissance")
public Date getDnaissance() {
	return dnaissance;
}

}


