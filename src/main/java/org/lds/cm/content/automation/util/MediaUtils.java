package org.lds.cm.content.automation.util;


import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import javax.sound.sampled.*;
import java.net.URL;

public class MediaUtils
{
    public static boolean verifyAudio(String audio_file_url)
    {
        try {
            System.out.println(audio_file_url);

/*            URL url = new URL(audio_file_url);
            AudioStream as = new AudioStream(url.openStream());
            AudioData data = as.getData();
            ContinuousAudioDataStream cas = new ContinuousAudioDataStream(data);
            AudioPlayer.player.start(cas);
            AudioPlayer.player.stop(cas);
*/
/*            URL url = new URL(audio_file_url);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
*/

/*            URL url = new URL(audio_file_url);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            AudioFormat     format = audioIn.getFormat();
            DataLine.Info   info = new DataLine.Info( SourceDataLine.class, format );
            SourceDataLine auline = (SourceDataLine)AudioSystem.getLine( info );
            auline.open( format );
            auline.close();
*/
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
