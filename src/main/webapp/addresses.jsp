<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Rechercher une addresse</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>

</head>
<body>

	<div class="container mt-4">
		<h2 class="text-center">Rechercher une addresse</h2>
		<form action="searchAddress" method="post"
			class="form-inline justify-content-center my-4">
			<div class="form-group">
				<input type="text" class="form-control mr-2" name="search"
					placeholder="Recherche">
			</div>
			<input type="submit" class="btn btn-primary" value="Rechercher">
		</form>
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Adresse</th>
					<th>Ville</th>
					<th>Code postal</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="address" items="${requestScope.ADDRESSES}">
					<tr>
						<td>${address.properties.name}</td>
						<td>${address.properties.city}</td>
						<td>${address.properties.postcode}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<script type="text/javascript">
		$(function() {
			$("input[name='search']").autocomplete({
				source : function(request, response) {
					$.ajax({
						url : "autocomplete",
						type : "GET",
						dataType : "json",
						data : {
							term : request.term
						},
						success : function(data) {
							response(data);
						}
					});
				},
				minLength : 2
			});
		});
	</script>

</body>
</html>
