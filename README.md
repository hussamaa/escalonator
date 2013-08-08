# Escalonator
Software simulador de técnicas de escalonamento de sistemas em tempo real

## Execução
Para executar o build do 'Escalonator' são necessários:

* Java - É necessário que a versão 1.7 do java esteja instalada no sistema operacional do cliente e com variável de ambiente JAVA_HOME ou JRE_HOME configurada direcionando para o seu diretório de instalação.

* Apache Maven - É necessário possuir o Apache Maven versão 3.0.x configurado. Este software pode ser obtido por meio da URL: http://ftp.unicamp.br/pub/apache/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.tar.gz

Para configura-lo é bem simples, basta descompacta-lo na pasta de preferência, em seguida criar um link simbolico da sua pasta $(pwd)/bin/mvn para /usr/bin/mvn onde $(pwd) se refere a pasta onde ele esta instalado.

Com estes pre-requisitos satisfeitos, para executar o escalonator é bem simples, basta entrar no diretório **sources/scheduler/** e na pasta onde existe o arquivo **pom.xml** digitar o comando **mvn clean jfx:run**

O maven irá baixar as dependências necessárias para o projeto e em seguida irá compila-lo e executa-lo.
	
