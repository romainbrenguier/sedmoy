package com.github.romainbrenguier.story.character;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Name {
    public static List<String> menfirstNames = Arrays.asList(
            "Louis", "Charles", "Henri", "Philippe",
            "Pierre", "François", "Alexandre", "Antoine",
            "Nicolas", "Victor", "Edouard", "Guillaume",
            "Maximilien", "Gaston", "Auguste", "Albert",
            "André", "Arthur", "Baptiste",
            "Claude", "Damien", "Étienne", "Fabien",
            "Gabriel", "Hector", "Isidore", "Jacques",
            "Léon", "Marcel", "Maurice", "Olivier",
            "René", "Théodore", "Valentin", "Xavier.");

    public static List<String> womenfirstNames = Arrays.asList(
            "Amélie", "Antoinette", "Blanche", "Camille",
            "Caroline", "Céline", "Charlotte",
            "Clémence", "Colette", "Constance", "Delphine",
            "Éloïse", "Emilie", "Estelle", "Gabrielle",
            "Geneviève", "Isabelle", "Jacqueline", "Jeanne",
            "Josephine", "Juliette", "Justine", "Laurence",
            "Lucie", "Madeleine", "Margaux", "Marguerite",
            "Marie", "Mathilde", "Nathalie", "Odette",
            "Pauline", "Simone", "Sophie", "Victoire");

    public static List<String> familyNameStatus = Arrays.asList("Devereaux",
            "Beaumont", "Montclair",
            "Blanchard", "Fontaine", "Lefevre",
            "Duval", "Chevalier", "Bouchard", "Moreau",
            "Leclerc", "Dubois", "Deschamps",
            "De La Roche", "Leblanc", "Dupont", "Delaunay",
            "Armand", "Dufour", "Dubois", "Lavoie",
            "Charpentier", "Beauchamp",
            "Tremblay", "Gagnon", "Lemieux",
            "Leclercq", "Morel", "Belanger",
            "Savard", "Bertrand", "Lefebvre", "Levesque",
            "Gauthier", "Leblanc", "Dumas");

    public static List<String> familyNameCommon = Arrays.asList("Dubois",
            "Martin", "Dupont",
            "Durand", "Lefevre",
            "Moreau", "Girard",
            "Bernard", "Petit", "Leroy",
            "Renard", "Lambert", "Michel",
            "Roux", "Simon",
            "Faure", "Mercier",
            "Laurent", "Fournier",
            "Gauthier");

    public String first;
    public String last;

    public static Name make(Random r, boolean isWoman, boolean hasHighStatus) {
        final Name name = new Name();
        if (isWoman)
            name.first = womenfirstNames.get(r.nextInt(womenfirstNames.size()));
        else
            name.first = menfirstNames.get(r.nextInt(menfirstNames.size()));
        if (hasHighStatus)
            name.last = familyNameStatus.get(r.nextInt(familyNameStatus.size()));
        else
            name.last = familyNameCommon.get(r.nextInt(familyNameCommon.size()));
        return name;
    }

    @Override
    public String toString() {
        return first + " " + last;
    }
}
