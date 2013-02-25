<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>DRI REST Interface</title>
</head>
<body>

<h3>Short DRI REST API description:</h3>

Note! The operations listed below are secured with the Digest Authentication HTTP mechanism. 
The user/password pair was supplied to authorized parties.

<ul>

<li>
<pre>/dri/add_to_management/{datasetID}</pre> adds specified dataset to DRI management. 
Asynchronous computing checksums task is scheduled to be performed. The invocation returns
as soon as the task is successfully scheduled. When the checksums computation is completed,
a notification message is sent.

<li>
<pre>/dri/remove_from_management/{datasetID}</pre> removes specified dataset from DRI management. 
It simply unsets dataset as managed in AIR registry.

<li>
<pre>/dri/update_checksums/{datasetID}</pre> updates logical datas checksums for dataset. 
Asynchronous update checksums task is scheduled to be performed. The invocation returns
as soon as the task is successfully scheduled. When the checksums update is completed,
a notification message is sent.

<li>
<pre>/dri/update_single_item/{datasetID}/{itemID}</pre> updates checksum of a single logical data
within specified dataset. This operation is suitable when only a small part of the dataset has
changed. Asynchronous update checksum task is scheduled to be performed. The invocation returns
as soon as the task is successfully scheduled. When the checksum update is completed,
a notification message is sent.

<li>
<pre>/dri/validate_dataset/{datasetID}</pre> validates specified dataset on request.
Asynchronous validation task is scheduled to be performed. The invocation returns
as soon as the task is successfully scheduled. When the validation is completed,
a notification message is sent.

</ul>

</body>
</html>