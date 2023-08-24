package com.example.projetjava.Depot;

import com.example.projetjava.Connexion;
import com.example.projetjava.Prototype.EmployerModel;

import java.util.ArrayList;

public class EmployerRepository extends Connexion {
    public void getEmployes() {
        try {
            String query = "SELECT * FROM employe";
            System.out.println("SQL = " + query);

            statement = connection.createStatement();

            resultSet = statement.executeQuery(query);

            ArrayList<EmployerModel> listEmployers = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String prenom = resultSet.getString(3);
                String poste = resultSet.getString(4);
                String mdp = resultSet.getString(5);
                String date_embauche = resultSet.getString(6);
                listEmployers.add(new EmployerModel(id,name,prenom,poste,mdp,date_embauche));
            }

            for (int i = 0; i <= listEmployers.size();i++)
            {
                System.out.println(listEmployers.get(i).toString());
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        finally {
            try {
                resultSet.close();
                statement.close();
                dbDeconnexion();

            } catch (Exception e2) {
//				e2.printStackTrace();
                System.out.println(e2);
            }
        }
    }
}
