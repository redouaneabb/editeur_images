package ProjetFinale;

import javafx.scene.paint.Color;
// fait le filtre sepia
public class FiltreSepia extends FiltreCouleur{
    @Override
    protected Color calculerCouleur(Color color){
        double red= color.getRed();
        double green= color.getGreen();
        double blue= color.getBlue();

        double newR=(red*0.393)+(green*0.769)+(blue*0.189);
        double newG=(red*0.349)+(green*0.686)+(blue*0.168);
        double newB=(red*0.272)+(green*0.534)+(blue*0.131);
        // Math.min permet de verifier que les valeurs ne font pas plus de 1.0 si ca depasse on renvoie du blanc
        return new Color(Math.min(1.0,newR),Math.min(1.0,newG),Math.min(1.0,newB),color.getOpacity());
    }
}
