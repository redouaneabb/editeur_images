
package ProjetFinale;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ImageDAO {
    //ObjectMapper=outil de jackson pour faire la convertion
    private final ObjectMapper mapper;

    public ImageDAO() {
        this.mapper=new ObjectMapper();
    }
    //sauvegarde les donnees dans fichier JSON
    public void sauvegarder(ImageModel imagemod,String nomFicJson) throws IOException {
        //transofrme obj en json et ecrit dans le fichier
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(nomFicJson),imagemod);
        System.out.println("Sauvegarde réussie dans : "+nomFicJson);
    }

    //charge les donnees depuis fichier JSON
    public ImageModel charger(String nomFicJson) throws IOException {
        ImageModel imagemod=this.mapper.readValue(new File(nomFicJson),ImageModel.class);

        if(imagemod.getNomFic()!=null){
            imagemod.setFichier(new File("resources/"+imagemod.getNomFic()));
        }
        return imagemod;
    }
}

