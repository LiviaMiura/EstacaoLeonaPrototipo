/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.estacaoLeona.prototipo;

import gnu.io.CommPortIdentifier;
import java.util.Enumeration;
import javax.swing.JButton;

/**
 * @author klauder
 */
public final class Arduino {

    private final ControlePorta arduino;
    CommPortIdentifier cpi = null;
    private String portaCOM;
    private int AzGraus, ElGraus, valorAtual;
    private String left, right, down, up;

    /**
     * Construtor da classe Arduino
     */
    public Arduino() {
        porta();
        arduino = new ControlePorta(portaCOM, 9600);

    }

    /*
     * Descobre quais portas de comunicação estão disponíveis
     */
    public void porta() {

        try {
            Enumeration pList = CommPortIdentifier.getPortIdentifiers();
            System.out.println("Porta =: " + pList.hasMoreElements());

            while (pList.hasMoreElements()) {
                cpi = (CommPortIdentifier) pList.nextElement();

                System.out.println("Portas " + cpi.getName() + " ");
                portaCOM = cpi.getName();

            }
            System.out.println("PortA ESCOLHIDA =" + portaCOM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     *Calculo da Azimute
     */
    public int calculoAzimute(JButton button, String grausDigitado) {

        int graus = Integer.parseInt(grausDigitado);

        try {
            if ("Pan".equals(button.getActionCommand())) {
                System.out.println("***************Calculo**AZIMUTE**********");
                if (graus >= 0 && graus <= 350) {

                    if (AzGraus < graus) {
                        int c = graus - AzGraus;
                        System.out.println("Calculo =" + graus + "-" + AzGraus + " = " + c);
                        right(c);
                    } else if (AzGraus > graus) {
                        int dif = AzGraus - graus;
                        System.out.println("Calculo =" + AzGraus + "-" + graus + " = " + dif);
                        left(dif);
                    } else if (graus == 0) {
                        int dif = AzGraus - AzGraus;
                        System.out.println("Calculo =" + AzGraus + "-" + AzGraus + " = " + dif);
                        left(dif);
                    }

                }
                AzGraus = graus;

            }
            System.out.println("Azimute graus digitado =" + AzGraus);
        } catch (Exception e) {
            System.out.println("*****Erro ao calcular azimute 0º a 350º* *****");
        }
        return graus;
    }

    /*
     *Calculo Elevação
     */
    public int calculoElevacao(JButton button, String grausDigitado) {
        int graus = Integer.parseInt(grausDigitado);

        try {
            if ("Tilt".equals(button.getActionCommand())) {
                if (graus > 0 && graus <= 35) {
                    if (valorAtual < graus) {
                        int c = graus - valorAtual;
                        up(c);
                        System.out.println("Resultado =" + c);
                    } else if (valorAtual > graus) {
                        int dif = valorAtual - graus;
                        down(dif);
                        System.out.println("Resultado =" + dif);
                    } else if (graus == 0) {
                        int dif = valorAtual - valorAtual;
                        down(dif);
                        System.out.println("Resultado =" + dif);
                    }
                } else if (graus <= 0 && graus >= -35) {
                    if (valorAtual == graus) {
                        System.out.println("********valorAtualfor========*NUMERO*NEGATIVO*********");
                        System.out.println("Graus negativo" + graus);
                        System.out.println("Valor Atual = " + valorAtual);
                        int c = valorAtual;
                        System.out.println("Resultado =" + c);

                    } else if (valorAtual < graus) {
                        System.out.println("********valorAtualfor<<<<<<NUMERO*NEGATIVO*********");
                        System.out.println("Graus negativo" + graus);
                        System.out.println("Valor Atual = " + valorAtual);
                        int c = graus - valorAtual;
                        up(c);
                        System.out.println("Resultado =" + c);

                    } else if (valorAtual > graus && valorAtual != 0) {
                        System.out.println("********valorAtualfor>>>>*NUMERO*NEGATIVO*********");
                        System.out.println("Graus negativo" + graus);
                        System.out.println("Valor Atual = " + valorAtual);
                        int c = valorAtual - graus;
                        down(c);
                        
                        System.out.println("Resultado =" + c);
                    } else if (valorAtual == 0) {
                        System.out.println("Elevação === 0");
                        System.out.println("Graus negativo" + graus);
                        System.out.println("Valor Atual = " + valorAtual);
                        int dif = -graus;
                        down(dif);
                        System.out.println("Resultado =" + dif);
                    }
                }
                valorAtual = graus;
            }
        } catch (Exception e) {
            System.out.println("*****Erro ao calcular tilt -35º 0º 35º*****");
        }
        return 1;
    }
    /*
     *Move para Esquerda
     */

    public int left(int graus) {
        if (graus < 350) { //limite de elevação 350º
            if (graus < 10) {
                left = "!00" + graus + "L*";
                System.out.println("LEFT = " + left);
                arduino.enviaDados(left);
            } else if (graus >= 10 && graus < 100) {
                left = "!0" + graus + "L*";
                System.out.println("LEFT = " + left);
                arduino.enviaDados(left);
            } else {
                left = "!" + graus + "L*";
                System.out.println("LEFT = " + left);
                arduino.enviaDados(left);
            }
        } else {
            System.out.println(" EXCEDE O LIMITE DE AZIMUTE left PERMITIDO");
        }
        return 1;
    }
    /*
     *Move para Direita
     */

    public int right(int graus) {
        if (graus <= 350) { //limite de elevação 350º
            if (graus < 10) {
                right = "!00" + graus + "R*";
                System.out.println("RIGHT = " + right);
                arduino.enviaDados(right);
            } else if (graus >= 10 && graus < 100) {
                String right2 = "!0" + graus + "R*";
                System.out.println("RIGHT = " + right2);
                arduino.enviaDados(right2);
            } else {
                String right3 = "!" + graus + "R*";
                System.out.println("RIGHT = " + right3);
                arduino.enviaDados(right3);
            }
        } else {
            System.out.println(" EXCEDE O LIMITE DE AZIMUTE right PERMITIDO");
        }
        return 1;
    }

    /*
     *Move para Cima
     */
    public int up(int graus) {
        if (graus <= 70) { //limite de elevação 35º
            if (graus < 10) {
                up = "!00" + graus + "U*";
                System.out.println("UP = " + up);
                arduino.enviaDados(up);

            } else {
                String up2 = "!0" + graus + "U*";
                System.out.println("UP = " + up2);
                arduino.enviaDados(up2);
            }
        } else {
            System.out.println(" EXCEDE O LIMITE DE ELEVAÇÃO up PERMITIDO");
        }
        return 1;
    }

    /*
     *Move para Baixo
     */
    public int down(int graus) {
        if (graus <= 70) { //limite de elevação 35º
            if (graus < 10) {
                String down = "!00" + graus + "D*";
                System.out.println("DOWN = " + down);
                arduino.enviaDados(down);
            } else {
                String x3 = "!0" + graus + "D*";
                System.out.println("DOWN = " + x3);
                arduino.enviaDados(x3);
            }
        } else {
            System.out.println(" EXCEDE O LIMITE DE ELEVAÇÃO down PERMITIDO");
        }
        return 1;

    }

    /*
     *Liga e desliga a camera teste swing
     */
    public int cameraON() {
        System.out.println("camera on");

        arduino.enviaDados("!111O*");//camera ON

        return 1;
    }

    public int cameraOFF() {
        System.out.println("camera off");
        arduino.enviaDados("!111F*");//camera ON
        return 1;
    }

    /*
     *Reset o pantilt azinmute 0º 
     */
    public int resetAzimute() {
        System.out.println("Reset Azimute");
        left = "!350L*";
        arduino.enviaDados(left);

        return 1;
    }
    /*
     *Reset o pantilt elevação 0º 
     */

    public int resetElevacao() {
        System.out.println("Reset elevação a novo Calculo");
        down = "!070D*";
        System.out.println("Reset elevação para tudo para baixo = " + down);
        arduino.enviaDados(down);
        String reset = "!035U*";
        arduino.enviaDados(reset);
        System.out.println("Reset elevação para 0º = " + reset);
        return 1;
    }

    /*
     * Chamada do método que fecha a comunicação com a porta serial
     */
    public int close() {
        System.out.println("close");
        arduino.close();
        return 1;
    }

}
