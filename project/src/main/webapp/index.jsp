<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<body>
<h2>DRI Service works!</h2>

<h3>Aktualna implementacja serwisu DRI:</h3>
<ul>
	<li> walidacji danych na podstawie sumy SHA-256 całości pliku,
	<li> walidacja periodyczna oraz na żądanie,
	<li> asynchroniczność wywołań - rezultaty w postaci serwisu notyfikacji,
	<li> obsługa Swifta oraz Amazon S3,
	<li> konfigurawalność (tj. okres walidacji, liczba wątków obsługujących) - wymaga dopracowania.
</ul>

<h3>Obecnie pracuję nad:</h3>
<ol>
	<li> dopracowaniem szczegółów implementacyjnych (kilka dodatków w API rejestru AIR),
	<li> <strong>opracowaniem algorytmu efektywnej walidacji (nie po całości pliku, ale hamują założenia projektu np. brak modyfikacji danych),</strong>
	<li> zabraniem się do napisania pracy (tekstu).
</ol>

<h3>DRI REST Interface:</h3>
The description of the DRI REST Interface is available here: /interface.jsp

<h3>Used technologies:</h3>
<ul>
	<li> <strong>Jersey API</strong> - REST interface and integration with AIR registry,
	<li> <strong>JCloud</strong>s - generic interface for all Storage Clouds,
	<li> <strong>Quartz</strong> - scheduling for periodic validation,
	<li> <strong>Guice</strong> - dependency injection and AOP,
	<li> <strong>Maven</strong> - build & dependencies
</ul>

</body>
</html>
