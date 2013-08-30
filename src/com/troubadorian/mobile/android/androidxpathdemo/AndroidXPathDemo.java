package com.troubadorian.mobile.android.androidxpathdemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

public class AndroidXPathDemo extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // parse the XML as a W3C Document
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory
                .newInstance();
        builderFactory.setNamespaceAware(true);
        /* start of copying files from assets to cache */

        AssetManager assetManager = getAssets();

        String[] files = null;

        try
        {
            files = assetManager.list("cachefiles");

        } catch (IOException e)
        {
            Log.e(this.getClass().getSimpleName(), e.toString());
        }
        String path = getBaseContext().getApplicationInfo().dataDir;
        Log.d(this.getClass().getSimpleName(), "=============================" + path);
        for (int i = 0; i < files.length; i++)
        {
            InputStream in = null;
            OutputStream out = null;

            try
            {
                in = assetManager.open("cachefiles/" + files[i]);
                out = new FileOutputStream(path + "/" + files[i]);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;

            } catch (IOException ex)
            {

            }
        }

        /* end of copying files from assets to cache */
        
        
        try
        {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            File file = new File(path + "/widgets.xml");

            Document document = builder.parse(file);

            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = "/widgets/widget";
            Node widgetNode = (Node) xpath.evaluate(expression, document,
                    XPathConstants.NODE);

            XPath xpath2 = XPathFactory.newInstance().newXPath();
            String expression2 = "manufacturer";
            Node manufacturerNode = (Node) xpath2.evaluate(expression2,
                    widgetNode, XPathConstants.NODE);

            Log.d(this.getClass().getSimpleName(), "-----------------------"
                    + manufacturerNode.getTextContent());

        } catch (Exception ex)
        {
            
            /* Used this as reference 
             * http://stackoverflow.com/questions/4524586/retrieve-value-of-xml-node-and-node-attribute-using-xpath-in-jaxp
             */
            Log.e(this.getClass().getSimpleName(),
                    "=====================" + ex.toString());
        }

    }

    private void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }
}