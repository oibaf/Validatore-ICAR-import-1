# Validatore-ICAR-import-1
Strumenti e schemi per validare file in formato XML conformi ai tracciati ICAR Import 1.0

## utilizzo standard

Per validare un file XML, spostarsi nel direttorio che contiene "validate.jar" e dare il comando

```
java -jar validate.jar -v <path-del-file-xml-da-validare> import-1/icar-import.xsd
```

## utilizzo offline

Per utilizzare il validatore in assenza di collegamento internet - o volendo comunque accelerare l'operazione, evitando lo scaricamento dal web dei vari XML Schema - si può utilizzare gli XML Schema presenti nel direttorio `import-1-offline`.

Il comando, in tal caso, diventa


```
java -jar validate.jar -v <path-del-file-xml-da-validare> import-1-offline/icar-import.xsd 
```


