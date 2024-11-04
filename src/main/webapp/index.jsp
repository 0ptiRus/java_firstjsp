<html>
	<head>
		<style>
			<%@include file="/WEB-INF/style.css"%>
		</style>
	</head>	
	<body>
		<div>
			<form action="MoneyTrack" method="post">
				<div>
					<label for="money">Amount:</label>
					<input type="number" name="money" id="money">	
					<label for="reason">Reason:</label>
					<input name="reason" id="reason">
					<button>Send</button>
				</div>
			</form>
			<div>
			<form action="MoneyTrack" method="get">
				<div>
					<button>Get all entries</button>
				</div>
			</form>
				
			</div>
		</div>
	</body>
</html>
