// role de cette partie:
//stocker ce qu'est une image pour l'app

package ProjetFinale;

import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class ImageModel {
    @JsonIgnore
    private File fichier;
    //les tags permettront de retrouver (categoriser) une image
    private String nomFic;
    private List<String> tags;
    private boolean estChiffre;
    @JsonIgnore
    private Image imageActive;

    // pour part 3 on ajouite une liste qui stocke l'ordre des transfos
    private List<String> historiqueTransfos;

    //on ajoute constructeur par defaut pour JACKSON
    public ImageModel(){
        this.tags=new ArrayList<>();
        this.historiqueTransfos=new ArrayList<>();
    }

    public ImageModel(File fichier) {
        this();
        this.fichier=fichier;
        this.nomFic=fichier.getName();
        this.estChiffre=false;
    }



    public void ajouterTag(String tag) {
        if(tag!=null && !tag.isEmpty()){
            boolean existeDeja=false;

            for(String t:this.tags){
                if(t.equals(tag)){
                    existeDeja=true;
                    break;
                }
            }
            if(!existeDeja){
                this.tags.add(tag);
            }
        }
    }

    public String getNomFic() {
        return nomFic;
    }

    public Image getImageActive(){
        return imageActive;
    }
    public void setImageActive(Image imageActive){
        this.imageActive=imageActive;
    }

    //permet d'enregistrer qu'un filtre ai ete applique
    public void enregistrerTransfos(String nomTransfos) {
        this.historiqueTransfos.add(nomTransfos);
    }

    public File getFichier() {return this.fichier;}
    public void setFichier(File fichier) {this.fichier=fichier;}

    public List<String> getTags() {return new ArrayList<>(this.tags);}
    public void setTags(List<String> tags) {
        this.tags.clear();
        if(tags!=null){
            this.tags.addAll(tags);
        }
    }

    public boolean isEstChiffre() {return this.estChiffre;}
    public void setEstChiffre(boolean estChiffre) {this.estChiffre=estChiffre;}

    public List<String> getHistoriqueTransfos() {return new ArrayList<>(this.historiqueTransfos);}
    public void setHistoriqueTransfos(List<String> historiqueTransfos) {
        this.historiqueTransfos.clear();
        if(historiqueTransfos!=null){
            this.historiqueTransfos.addAll(historiqueTransfos);
        }
    }
}
