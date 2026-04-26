# Usa uma imagem base com Java
FROM eclipse-temurin:17-jdk AS build

# Copia o código para dentro do container
WORKDIR /app
COPY . .

# Compila o projeto (Maven wrapper)
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Segunda etapa: imagem leve só com o jar
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o jar gerado da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Comando de inicialização
CMD ["java", "-jar", "app.jar"]
