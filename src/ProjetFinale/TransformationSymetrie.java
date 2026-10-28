package ProjetFinale;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
// on fait une transformations miroir
public class TransformationSymetrie implements Filtre{
    @Override
    public Image appliquer(Image imageEntre){
        int largeur=(int) imageEntre.getWidth();
        int hauteur=(int) imageEntre.getHeight();

        WritableImage sortie = new WritableImage(largeur,hauteur);
        PixelReader lecteur = imageEntre.getPixelReader();
        PixelWriter ecriture=sortie.getPixelWriter();

        for(int y=0;y<hauteur;y++){
            for(int x=0;x<largeur;x++){
                //on prend le pixel a x et on l'ecrit a largeur-1-x
                ecriture.setArgb(largeur-1-x,y,lecteur.getArgb(x,y));
            }
        }
        return sortie;
    }
}
