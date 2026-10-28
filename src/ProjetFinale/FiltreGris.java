package ProjetFinale;


import javafx.scene.paint.Color;
// va faire la moyenne de la couleur recu pour le noir et blanc (gris)
public class FiltreGris extends FiltreCouleur{
    @Override
    protected Color calculerCouleur(Color color){
        double moyenne=((color.getRed()+ color.getBlue()+ color.getGreen())/3.0);
        return new Color(moyenne,moyenne,moyenne,color.getOpacity());

    }
}
