package proitdevelopers.com.androidquizfirebase.Common;

import java.util.ArrayList;
import java.util.List;

import proitdevelopers.com.androidquizfirebase.Model.PerguntaErrada;
import proitdevelopers.com.androidquizfirebase.Model.Question;
import proitdevelopers.com.androidquizfirebase.Model.User;

public class Common {

    public static String categoryId,categoryName;
    public static User currentUser;
    public static List<Question> questionList = new ArrayList<>();
    public static List<PerguntaErrada> questErradasList = new ArrayList<>();

}
