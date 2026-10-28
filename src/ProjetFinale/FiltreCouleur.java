package ProjetFinale;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public abstract class FiltreCouleur implements Filtre {
    @Override
    public Image appliquer(Image imageEntree){
        int largeur=(int)imageEntree.getWidth();
        int hauteur=(int)imageEntree.getHeight();

        //creation copie vide
        WritableImage imageSortie=new WritableImage(largeur,hauteur);
        PixelReader lecteur=imageEntree.getPixelReader();
        PixelWriter ecriture= imageSortie.getPixelWriter();

        //on parcourt chaque px de matrice de l'image
        for(int y=0;y<hauteur;y++){
            for(int x=0;x<largeur;x++){
                Color couleurOrigine=lecteur.getColor(x,y);

                //on demande de calculer nouvelle couleure different pour chaque filtre
                Color nouvelleCouleur=calculerCouleur(couleurOrigine);

                ecriture.setColor(x,y,nouvelleCouleur);
            }
        }
        return imageSortie;
    }
    // les classes enfants vont cacluler pour la classe mere
    protected abstract Color calculerCouleur(Color couleurOrigine);
}
