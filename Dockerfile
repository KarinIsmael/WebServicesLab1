FROM adoptopenjdk:16-jre
COPY core/target/classes /app/core/
COPY core/target/classes /app/core/
COPY core/target/web /web
ENTRYPOINT ["java", "--module-path", "/app/core:/app/modules", "-m", "core/core.Main"]