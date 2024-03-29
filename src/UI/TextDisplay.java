package UI;


import Game.NPC;
import Imported.Texture;
import LowLevel.CharTex;
import LowLevel.Geometrical;
import LowLevel.Image;

public class TextDisplay {
	public static CharTex[] fontChars = new CharTex[53];
	//Have geo, str, size, frame
	public static void showText(Image textBox, String str, double fontSize, int frameNum)
    {
    	showText(textBox, str, fontSize, frameNum, textBox.getAlpha());
    }
	//Have geo, list, size
	public static void showText(Image textBox, char[] charLi, double fontSize)
    {
    	showText(textBox, charLi, fontSize, charLi.length);
    }
	//Have geo, str, size, alpha
	public static void showText(Image textBox, String str, double fontSize, float alpha)
    {
    	showText(textBox, str, fontSize, str.length(), alpha);
    }
	//Have geo, list, size, alpha
	public static void showText(Image textBox, char[] charLi, double fontSize, float alpha)
    {
    	showText(textBox, charLi, fontSize, charLi.length, alpha);
    }
	//Have geo, str, size
	public static void showText(Image textBox, String str, double fontSize)
    {
    	showText(textBox, str, fontSize, str.length());
    }
	//Have geo, listChars, size, frame
	public static void showText(Image textBox, char[] charLi, double fontSize, int frameNum)
	{
		showText(textBox, charLi, fontSize,  frameNum, textBox.getAlpha());
	}
	//Have geo, str, size, num, alpha
	public static void showText(Image textBox, String str, double fontSize, int frameNum, float alpha)
    {
    	showText(textBox, toChars(str), fontSize, frameNum, alpha);
    }
	public static void showText(Image textBox, char[] charLi, double fontSize, int frameNum, float a)
	{
		showText(textBox, charLi, fontSize, frameNum, a, 0, 0, 0);
	}
	
	//FrameNum decides how many chars to display, allows us to display one char at a time
    public static void showText(Image textBox, char[] charLi, double fontSize, int frameNum, float a, float r, float g, float b)
    {
    	textBox.UIshow();
    	double texX = textBox.getX() - textBox.getWidth() / 2 + fontSize;
    	double texY = textBox.getY() + textBox.getLength() / 2 - fontSize;
    	Image letterImg;
    	
    	
    	//Draw each character in the charLi string to the screen
    	for (int i = 0; i < frameNum && i < charLi.length; i++)
    	{
    		//If the char is nothing, we will display nothing :)
    		if (charLi[i] != ' ' && charLi[i] != '`')
    		{
    			CharTex currLetter = getLetter(charLi[i]);
    			texX -= (1 - currLetter.getSpace()) * fontSize;
    			letterImg = new Image(currLetter.getTex(), texX, texY, fontSize * currLetter.getSize(), fontSize * currLetter.getSize());
    			letterImg.UIshow(r, g, b, a);
    			texX += currLetter.getSpace() * fontSize;
    			if (charLi[i] == '-') {}
    		}
    		else if (charLi[i] == ' ') 
    		{
				texX += fontSize;
			}
    		if (texX >= textBox.getX() + textBox.getWidth() / 2 - fontSize / 2 || charLi[i] == '`') 
    		{
    			texX = textBox.getX() - textBox.getWidth() / 2 + fontSize;
    			texY -= fontSize;
    		}
    	}
    	
    }
    
    //Given a char, returns the corresponding font texture
    public static CharTex getLetter(char letter)
    {
    	switch (letter) {
			case ',': return fontChars[26];
			case ';': return fontChars[27];
			case '!': return fontChars[28];
			case '#': return fontChars[29];
			case ':': return fontChars[41];
			case '/': return fontChars[43];
			case '?': return fontChars[42];
			case '(': return fontChars[47];
			case ')': return fontChars[48];
			case '@': return fontChars[44];
			case '%': return fontChars[46];
			case '&': return fontChars[45];
			case '$': return fontChars[40];
			case '.': return fontChars[49];
			case '\'': return fontChars[50];
			case '+': return fontChars[51]; 
			case '-': return fontChars[52];
		}
    	int base = 0;
    	int letterVal = letter;
		if (letterVal >= 48 && letterVal < 58) {base = 18;}
		if (letterVal >= 65 && letterVal <= 96) {base = 65;}
		if (letterVal >= 97 && letterVal <= 122) {base = 97;}
		return fontChars[letterVal - base];
    }
    public static void initText()
    {
    	fontChars[0] = new CharTex(new Texture("Font/A.png"));
    	fontChars[1] = new CharTex(new Texture("Font/B.png"));
    	fontChars[2] = new CharTex(new Texture("Font/C.png"));
    	fontChars[3] = new CharTex(new Texture("Font/D.png"));
    	fontChars[4] = new CharTex(new Texture("Font/E.png"));
    	fontChars[5] = new CharTex(new Texture("Font/F.png"));
    	fontChars[6] = new CharTex(new Texture("Font/G.png"));
    	fontChars[7] = new CharTex(new Texture("Font/H.png"));
    	fontChars[8] = new CharTex(new Texture("Font/I.png"), .8);
    	fontChars[9] = new CharTex(new Texture("Font/J.png"), .85);
    	fontChars[10] = new CharTex(new Texture("Font/K.png"));
    	fontChars[11] = new CharTex(new Texture("Font/L.png"), .85);
    	fontChars[12] = new CharTex(new Texture("Font/M.png"), 1.15, .98);
    	fontChars[13] = new CharTex(new Texture("Font/N.png"));
    	fontChars[14] = new CharTex(new Texture("Font/O.png"));
    	fontChars[15] = new CharTex(new Texture("Font/P.png"), .90, .98);
    	fontChars[16] = new CharTex(new Texture("Font/Q.png"), 1, .96);
    	fontChars[17] = new CharTex(new Texture("Font/R.png"));
    	fontChars[18] = new CharTex(new Texture("Font/S.png"), .96, 1.02);
    	fontChars[19] = new CharTex(new Texture("Font/T.png"));
    	fontChars[20] = new CharTex(new Texture("Font/U.png"));
    	fontChars[21] = new CharTex(new Texture("Font/V.png"));
    	fontChars[22] = new CharTex(new Texture("Font/W.png"), 1.15, .98);
    	fontChars[23] = new CharTex(new Texture("Font/X.png"));
    	fontChars[24] = new CharTex(new Texture("Font/Y.png"));
    	fontChars[25] = new CharTex(new Texture("Font/Z.png"));
    	fontChars[26] = new CharTex(new Texture("Font/,.png"));
    	fontChars[27] = new CharTex(new Texture("Font/;.png"), .6);
    	fontChars[28] = new CharTex(new Texture("Font/!.png"));
    	fontChars[29] = new CharTex(new Texture("Font/#.png"));
    	fontChars[30] = new CharTex(new Texture("Font/0.png"));
    	fontChars[31] = new CharTex(new Texture("Font/1.png"));
    	fontChars[32] = new CharTex(new Texture("Font/2.png"));
    	fontChars[33] = new CharTex(new Texture("Font/3.png"));
    	fontChars[34] = new CharTex(new Texture("Font/4.png"));
    	fontChars[35] = new CharTex(new Texture("Font/5.png"));
    	fontChars[36] = new CharTex(new Texture("Font/6.png"));
    	fontChars[37] = new CharTex(new Texture("Font/7.png"));
    	fontChars[38] = new CharTex(new Texture("Font/8.png"));
    	fontChars[39] = new CharTex(new Texture("Font/9.png"));
    	fontChars[40] = new CharTex(new Texture("Font/$.png"));
    	fontChars[41] = new CharTex(new Texture("Font/colon.png"), .6);
    	fontChars[42] = new CharTex(new Texture("Font/question.png"));
    	fontChars[43] = new CharTex(new Texture("Font/slash.png"));
    	fontChars[44] = new CharTex(new Texture("Font/@.png"));
    	fontChars[45] = new CharTex(new Texture("Font/&.png"));
    	fontChars[46] = new CharTex(new Texture("Font/%.png"));
    	fontChars[47] = new CharTex(new Texture("Font/(.png"));
    	fontChars[48] = new CharTex(new Texture("Font/).png"));
    	fontChars[49] = new CharTex(new Texture("Font/period.png"), .9);
    	fontChars[50] = new CharTex(new Texture("Font/apostrophe.png"), .6);
    	fontChars[51] = new CharTex(new Texture("Font/plus.png"));
    	fontChars[52] = new CharTex(new Texture("Font/minus.png"));
    	NPC.initText();
    }
    public static char[] toChars(String str)
    {
    	char[] list = new char[str.length()];
    	for (int i = 0; i < list.length; i++) {list[i] = str.charAt(i);}
    	return list;
    }
}
