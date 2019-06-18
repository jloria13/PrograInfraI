
package Logic;

import Interfaz.vistaPrincipal;
import java.util.TimerTask;

public class Temporizer extends TimerTask {
    
    vistaPrincipal ventana;
    
    public Temporizer(vistaPrincipal ventana){
        this.ventana = ventana;
    }

    @Override
    public void run() {
        ventana.refreshTable();
        }
}
