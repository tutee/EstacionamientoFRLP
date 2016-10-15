package com.tute.estacionamientofrlp;

import java.util.ArrayList;

/**
 * Created by Tute on 24/9/2016.
 */
public class VarGlobales {
    public static String cSaldo, cUid, cCompSem, cPosSpi, sem1, sem2, pridiasem,ultdiasem,semana, apellido, nombre, email, cRole;
    public static ArrayList cCod;
    public static Integer montoSum;
    public static double precioticket = 3;
    public static boolean cerror; //Checkea si la contraseña del usuario es válida.

    //Cuando actacargar vale 1, GetCompras carga CompraActivtiy, cuando vale 2 carga DeshacerActivity
    public static Integer actacargar;
}
