package com.chsapps.yt_hongjinyoung.ui.youtube_player;

import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.constants.PlayerConstants;
import com.chsapps.yt_hongjinyoung.service.YoutubePlayerService;
import com.chsapps.yt_hongjinyoung.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ConstantStrings {

//    public static int playSongPrevIdx = -1;
    public static int playSongIdx = 0;
    public static ArrayList<String> ARRAY_P_LIST = new ArrayList<>();
    public static ArrayList<Integer> ARRAY_INDEX_LIST = new ArrayList<>();
    public static Map<String, Boolean> MAP_PLAY_SONG = new HashMap<>();

    public static String PLAYLIST_VIDS = "";
    public static String VID = "";

    public static void setVid(String vid) {
        ConstantStrings.VID = vid;
    }

    public static String getVideoHTML() {
        return getPlayListHTML();
//        return  "<!DOCTYPE HTML>\n" +
//                "<html>\n" +
//                "  <head>\n" +
//                "    <script src=\"https://www.youtube.com/iframe_api\"></script>\n" +
//                "    <style type=\"text/css\">\n" +
//                "        html, body {\n" +
//                "            margin: 0px;\n" +
//                "            padding: 0px;\n" +
//                "            border: 0px;\n" +
//                "            width: 100%;\n" +
//                "            height: 100%;\n" +
//                "        }\n" +
//                "    </style>" +
//                "  </head>\n" +
//                "\n" +
//                "  <body>\n" +
//                "    <iframe style=\"display: block;\" id=\"player\" frameborder=\"0\"  width=\"100%\" height=\"100%\" " +
//                "       src=\"https://www.youtube.com/embed/" + VID +
//                       "?enablejsapi=1&autoplay=1&iv_load_policy=3&fs=0&rel=0\">" +
//                "    </iframe>\n" +
//                "    <script type=\"text/javascript\">\n" +
//                "      var tag = document.createElement('script');\n" +
//                "      tag.src = \"https://www.youtube.com/iframe_api\";\n" +
//                "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
//                "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
//                "      var player;\n" +
//                "      function onYouTubeIframeAPIReady() {\n" +
//                "          player = new YT.Player('player', {\n" +
//                "              events: {\n" +
//                "                  'onReady': onPlayerReady\n" +
//                "              }\n" +
//                "          });\n" +
//                "      }\n" +
//                "      function onPlayerReady(event) {\n" +
//                "          player.setPlaybackQuality(\"" + PlayerConstants.getPlaybackQuality() + "\");\n" +
//                "      }\n" +
//                "    </script>\n" +
//                "\n" +
//                "</body>\n" +
//                "</html>";
    }

    public static String getCurrentSongVideoId() {
        try {
            return ARRAY_P_LIST.get(playSongIdx);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getNextSongIndex() {
        int repeatType = Global.getInstance().getRepeatType();
        int randomType = Global.getInstance().getRandomType();

        int randomIndex = 0;
        for(String vid : MAP_PLAY_SONG.keySet()) {
            if(!MAP_PLAY_SONG.get(vid)) {
                randomIndex++;
            }
        }

        /**
         * 0 : none repeat
         * 1 : repeat all
         * 2 : repeat one song
         * */
        switch (repeatType) {
            case 0:
                if(randomIndex == 0) {
                    MAP_PLAY_SONG.clear();
                    for(String vid : ARRAY_P_LIST) {
                        MAP_PLAY_SONG.put(vid, false);
                    }
                    return -1;
                }
                break;
            case 1:
                if(randomIndex == 0) {
                    MAP_PLAY_SONG.clear();
                    for(String vid : ARRAY_P_LIST) {
                        MAP_PLAY_SONG.put(vid, false);
                    }
                    randomIndex = MAP_PLAY_SONG.size();
                }
                break;
            case 2:
                LogUtil.e("PLAY.", "4) repeatType == 2");
                return playSongIdx;
        }

        /**
         * 0 :  none random
         * 1 : random
         * */
        if(randomType == 0) {
            return playSongIdx + 1;
        } else {
            Random random = new Random();
            int randomNum = random.nextInt(randomIndex);
            int index = 0, falseIndex = 0;
            for(String vid : MAP_PLAY_SONG.keySet()) {
                if(!MAP_PLAY_SONG.get(vid)) {
                    if(randomNum == falseIndex) {
                        return index;
                    }
                    falseIndex++;
                }
                index++;
            }
        }
        return 0;
    }
    public static boolean setNextSongIndex() {
        //playSongPrevIdx = playSongIdx;
        int songIdx = getNextSongIndex();
//        LogUtil.e("PLAY.", "RESULT) " + songIdx);
        if(songIdx == -1) {
            return false;
        }
        playSongIdx = songIdx;
        if(ARRAY_P_LIST.size() <= playSongIdx) {
            playSongIdx = 0;
        }
        YoutubePlayerService.setTotalTime();
        return true;
    }

    public static void setPrevSongIndex() {
        playSongIdx--;
        if(playSongIdx < 0) {
            playSongIdx = ARRAY_P_LIST.size() - 1;
        }
    }

    public static int getCurrentSongAPIIndex() {
        try {
            return ARRAY_INDEX_LIST.get(playSongIdx);
        } catch (Exception e) {
        }
        return -1;
    }

    public static void setPList(String playSong, String PList, String PSongIdxList) {
        ARRAY_P_LIST.clear();
        MAP_PLAY_SONG.clear();
        ARRAY_INDEX_LIST.clear();
        {
            int index = 0;
            String[] splitString = PList.split(",");
            for (String vid : splitString) {
                if (vid.equals(playSong)) {
                    playSongIdx = index;
                }
                ARRAY_P_LIST.add(vid);
                MAP_PLAY_SONG.put(vid, false);
                index++;
            }
        }

        {

            String[] splitString = PSongIdxList.split(",");
            for (String strIdx : splitString) {

                try {
                    int idx = Integer.parseInt(strIdx);
                    ARRAY_INDEX_LIST.add(idx);
                } catch (Exception e) {
                }
            }
        }

        PLAYLIST_VIDS = PList;
    }

    public static String getPlayListHTML() {
        MAP_PLAY_SONG.put(ARRAY_P_LIST.get(playSongIdx), true);
        return "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <script src=\"https://www.youtube.com/iframe_api\"></script>\n" +
                "    <style type=\"text/css\">\n" +
                "        html, body {\n" +
                "            margin: 0px;\n" +
                "            padding: 0px;\n" +
                "            border: 0px;\n" +
                "            width: 100%;\n" +
                "            height: 100%;\n" +
                "        }\n" +
                "    </style>" +
                "  </head>\n" +
                "\n" +
                "  <body>\n" +
                "    <iframe style=\"display: block;\" id=\"player\" frameborder=\"0\"  width=\"100%\" height=\"100%\" " +
                "       src=\"https://www.youtube.com/embed/" + ARRAY_P_LIST.get(playSongIdx) +
                "?enablejsapi=1&autoplay=1&iv_load_policy=3&fs=0&rel=0\">" +
                "    </iframe>\n" +
                "    <script type=\"text/javascript\">\n" +
                "      var tag = document.createElement('script');\n" +
                "      tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                "      var player;\n" +
                "      function onYouTubeIframeAPIReady() {\n" +
                "          player = new YT.Player('player', {\n" +
                "              events: {\n" +
                "                  'onReady': onPlayerReady\n" +
                "              }\n" +
                "          });\n" +
                "      }\n" +
                "      function onPlayerReady(event) {\n" +
                "          player.setPlaybackQuality(\"" + PlayerConstants.getPlaybackQuality() + "\");\n" +
                "      }\n" +
                "      function getDuration() {\n" +
                "       player.setPlaybackQuality(\"" + PlayerConstants.getPlaybackQuality() + "\");\n" +
                "      }\n" +
                "    </script>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

//        return "<!DOCTYPE HTML>\n" +
//                "<html>\n" +
//                "  <head>\n" +
//                "    <script src=\"https://www.youtube.com/iframe_api\"></script>\n" +
//                "    <style type=\"text/css\">\n" +
//                "        html, body {\n" +
//                "            margin: 0px;\n" +
//                "            padding: 0px;\n" +
//                "            border: 0px;\n" +
//                "            width: 100%;\n" +
//                "            height: 100%;\n" +
//                "        }\n" +
//                "    </style>" +
//                "  </head>\n" +
//                "\n" +
//                "  <body>\n" +
//                "    <iframe style=\"display: block;\" id=\"player\" frameborder=\"0\" width=\"100%\" height=\"100%\" " +
//                "       src=\"https://www.youtube.com/embed/" +
//                        "?list=" + PLAYLIST_VIDS +
//                        "&enablejsapi=1&autoplay=1&iv_load_policy=3&fs=0&rel=0\">" +
//                "    </iframe>\n" +
//                "    <script type=\"text/javascript\">\n" +
//                "      var tag = document.createElement('script');\n" +
//                "\n" +
//                "      tag.src = \"https://www.youtube.com/iframe_api\";\n" +
//                "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
//                "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
//                "      var player;\n" +
//                "      function onYouTubeIframeAPIReady() {\n" +
//                "          player = new YT.Player('player', {\n" +
//                "              events: {\n" +
//                "                  'onReady': onPlayerReady\n" +
//                "              }\n" +
//                "          });\n" +
//                "      }\n" +
//                "      function onPlayerReady(event) {\n" +
//                "          player.setPlaybackQuality(\""+ PlayerConstants.getPlaybackQuality() +"\");\n" +
//                "      }\n" +
//                "    </script>\n" +
//                "\n" +
//                "</body>\n" +
//                "</html>";
    }
}
