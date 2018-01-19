package group16.mad.com.thegamesdb;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vipul.Shukla on 2/16/2017.
 */

/*
Assignment #Homework 5.
GamesUtil.java
Vipul Shukla, Shanmukh Anand*/

public class GamesUtil {

    static class GamePullParser {

        static ArrayList<Game> parseGameList(InputStream in) throws XmlPullParserException, IOException, ParseException {
            ArrayList<Game> gameList = new ArrayList<>();
            Game game = null;

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("Game")) {
                            game = new Game();
                        } else if (parser.getName().equals("id")) {
                            game.setId(Integer.parseInt(parser.nextText().trim()));
                        } else if (parser.getName().equals("GameTitle")) {
                            game.setTitle(parser.nextText().trim());
                        } else if (parser.getName().equals("ReleaseDate")) {
                            String releaseText = parser.nextText().trim();
                            if (releaseText != null && releaseText.length() > 0) {
                                String[] completeReleaseDate = releaseText.split("/");
                                game.setReleaseYear(completeReleaseDate[2]);
                            }
                        } else if (parser.getName().equals("Platform")) {
                            game.setPlatform(parser.nextText().trim());
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("Game")) {
                            gameList.add(game);
                            game = null;
                        }
                        break;

                    default:
                        break;
                }

                event = parser.next();
            }
            return gameList;
        }

        static Game parseGame(InputStream in) throws XmlPullParserException, IOException, ParseException {
            Game game = null;
            String imageBaseUrl = "";
            boolean firstImage = true;
            boolean isPrimaryId = true;
            List<Integer> similarGameIds = new ArrayList<>();

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                switch (event) {
                    case XmlPullParser.START_TAG:

                        if (parser.getName().equals("Data")) {
                            game = new Game();
                        } else if (parser.getName().equals("baseImgUrl")) {
                            imageBaseUrl = parser.nextText().trim();
                        } else if (parser.getName().equals("GameTitle")) {
                            game.setTitle(parser.nextText().trim());
                        } else if (parser.getName().equals("Overview")) {
                            game.setOverview(parser.nextText().trim());
                        } else if (parser.getName().equals("genre")) {
                            game.setGenre(parser.nextText().trim());
                        } else if (parser.getName().equals("Youtube")) {
                            game.setYoutubeUrl(parser.nextText().trim());
                        } else if (parser.getName().equals("Publisher")) {
                            game.setPublisher(parser.nextText().trim());
                        } else if (parser.getName().equals("thumb") && firstImage == true) {
                            game.setImageUrl(imageBaseUrl + parser.nextText().trim());
                            firstImage = false;
                        } else if (parser.getName().equals("id") && isPrimaryId == true) {
                            game.setId(Integer.parseInt(parser.nextText().trim()));
                            isPrimaryId = false;
                        } else if (parser.getName().equals("id") && isPrimaryId == false) {
                            similarGameIds.add(Integer.parseInt(parser.nextText().trim()));
                        } else if (parser.getName().equals("ReleaseDate")) {
                            String releaseText = parser.nextText().trim();
                            if (releaseText != null && releaseText.length() > 0) {
                                String[] completeReleaseDate = releaseText.split("/");
                                game.setReleaseYear(completeReleaseDate[2]);
                            }
                        }else if (parser.getName().equals("Platform")) {
                            game.setPlatform(parser.nextText().trim());
                        }

                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("Similar")) {
                            game.setSimilarGamesId(similarGameIds);
                        }
                        break;

                    default:
                        break;
                }

                event = parser.next();
            }
            return game;
        }

        static List<Game> parseMultipleGames(List<InputStream> inputStreamList) throws XmlPullParserException, IOException, ParseException {
            Game game = null;
            String imageBaseUrl = "";
            boolean firstImage = true;
            boolean isPrimaryId = true;
            List<Integer> similarGameIds = new ArrayList<>();
            List<Game> gameList = new ArrayList<>();

            for(InputStream in : inputStreamList) {

                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(in, "UTF-8");
                int event = parser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {

                    switch (event) {
                        case XmlPullParser.START_TAG:

                            if (parser.getName().equals("Data")) {
                                game = new Game();
                            } else if (parser.getName().equals("baseImgUrl")) {
                                imageBaseUrl = parser.nextText().trim();
                            } else if (parser.getName().equals("GameTitle")) {
                                game.setTitle(parser.nextText().trim());
                            } else if (parser.getName().equals("Overview")) {
                                game.setOverview(parser.nextText().trim());
                            } else if (parser.getName().equals("genre")) {
                                game.setGenre(parser.nextText().trim());
                            } else if (parser.getName().equals("Youtube")) {
                                game.setYoutubeUrl(parser.nextText().trim());
                            } else if (parser.getName().equals("Publisher")) {
                                game.setPublisher(parser.nextText().trim());
                            } else if (parser.getName().equals("thumb") && firstImage == true) {
                                game.setImageUrl(imageBaseUrl + parser.nextText().trim());
                                firstImage = false;
                            } else if (parser.getName().equals("id") && isPrimaryId == true) {
                                game.setId(Integer.parseInt(parser.nextText().trim()));
                                isPrimaryId = false;
                            } else if (parser.getName().equals("id") && isPrimaryId == false) {
                                similarGameIds.add(Integer.parseInt(parser.nextText().trim()));
                            } else if (parser.getName().equals("ReleaseDate")) {
                                String releaseText = parser.nextText().trim();
                                if (releaseText != null && releaseText.length() > 0) {
                                    String[] completeReleaseDate = releaseText.split("/");
                                    game.setReleaseYear(completeReleaseDate[2]);
                                }
                            } else if (parser.getName().equals("Platform")) {
                                game.setPlatform(parser.nextText().trim());
                            }

                            break;

                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("Similar")) {
                                game.setSimilarGamesId(similarGameIds);
                            }
                            break;

                        default:
                            break;
                    }

                    event = parser.next();
                }
                gameList.add(game);
            }
            return gameList;
        }
    }

}
