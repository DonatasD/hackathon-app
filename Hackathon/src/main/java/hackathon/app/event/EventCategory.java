package hackathon.app.event;

import hackathon.app.R;

/**
 * Created by don on 6/15/15.
 */
public class EventCategory {

    private final static String ADAPTATION = "Adaptations";
    private final static String INFO = "Advice and information";
    private final static String ARTS = "Arts and crafts";
    private final static String BNE = "Barnardo's North East";
    private final static String CARE = "Care and wellbeing";
    private final static String CLUBS = "Clubs, hobbies and interests";
    private final static String COMMUNITY = "Community groups and centres";
    private final static String CHILDHOOD = "Early Childhood Services";
    private final static String EEAST = "Early Education Additional Support Team (EEAST)";
    private final static String EDUCATION = "Education and learning";
    private final static String ENTERTAINMENT = "Entertainment";
    private final static String ENVIRONMENT = "Environment and parks";
    private final static String FAMILY = "Family Lives";
    private final static String FINANCE = "Finance";
    private final static String HEALTH = "Health";
    private final static String HEALTH_SUPPORT = "Health support";
    private final static String HEALTH_VENUES = "Health venues";
    private final static String HEXHAM_ROAD = "Hexham Road";
    private final static String HIGH_STREET = "High Street";
    private final static String HOUSING = "Housing and homelessness";
    private final static String JOBS = "Jobs and careers";
    private final static String LEISURE = "Leisure";
    private final static String MEDIA = "Media";
    private final static String MILLIN_CENTRE = "Millin Centre";
    private final static String MFC = "Monkchester Family Centre";
    private final static String MUSEUMS = "Museums and galleries";
    private final static String NAPI = "NAPI - Newcastle Action for Parent and Toddler Groups Initiative";
    private final static String NEBT = "Newcastle Eagles Basketball Team";
    private final static String SENIORS = "Older people";
    private final static String PARENTING = "Parenting and families";
    private final static String PHADS = "Percy Hedley Academy for Disability Sports";
    private final static String PERFORMING_ARTS = "Performing arts";
    private final static String PLAY = "Play";
    private final static String RELIGION = "Religion and beliefs";
    private final static String SCHOOL = "School";
    private final static String SPORTS = "Sports and active leisure";
    private final static String SSECC = "Sure Start East Children's Centre";
    private final static String SSWRCC = "Sure Start West Riverside Children's Centres";
    private final static String TOUR = "Tours and days out";
    private final static String TRANSPORT = "Transport";
    private final static String UNIFORMED_CLUB = "Uniformed club";
    private final static String VOLUNTEERING = "Volunteering and participation";
    private final static String WADL = "Walker Activity Dome and Library";
    private final static String YMCA = "YMCA Newcastle";
    private final static String YOUNG_PEOPLE = "Young people";

    public static int getImageByCategory(String category) {
        switch (category) {
            case INFO:
                return (R.drawable.info);
            case SPORTS:
                return R.drawable.sport;
            case ARTS:
                return R.drawable.art;
            case PERFORMING_ARTS:
                return R.drawable.art;
            case ADAPTATION:
                return R.drawable.adaptation;
            case CLUBS:
                return R.drawable.clubs;
            case CARE:
                return R.drawable.care;
            case COMMUNITY:
                return R.drawable.community;
            case CHILDHOOD:
                return R.drawable.childhood;
            case EDUCATION:
                return R.drawable.education;
            case ENTERTAINMENT:
                return R.drawable.enterntaiment;
            case ENVIRONMENT:
                return R.drawable.environment;
            case FAMILY:
                return R.drawable.family;
            case FINANCE:
                return R.drawable.finance;
            case HEALTH:
                return R.drawable.health;
            case HEALTH_SUPPORT:
                return R.drawable.health;
            case HEALTH_VENUES:
                return R.drawable.health;
            case HOUSING:
                return R.drawable.housing;
            case JOBS:
                return R.drawable.jobs;
            case LEISURE:
                return R.drawable.leisure;
            case MEDIA:
                return R.drawable.media;
            case MUSEUMS:
                return R.drawable.museum;
            case SENIORS:
                return R.drawable.senior;
            case PARENTING:
                return R.drawable.childhood;
            case RELIGION:
                return R.drawable.religion;
            case TOUR:
                return R.drawable.tour;
            case TRANSPORT:
                return R.drawable.transport;
            case VOLUNTEERING:
                return R.drawable.volunteer;
            case YOUNG_PEOPLE:
                return R.drawable.young_people;
            default:
                return R.drawable.default_event;
        }
    }
}
