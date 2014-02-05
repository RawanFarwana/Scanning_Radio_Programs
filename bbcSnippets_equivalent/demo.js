function build(fileName){
	// ["exported", "41.wav"], pick "41.wav", then ["41", "wav"] and then you pick "41"
	noOfBeeps = fileName.split("_")[1].split(".")[0];
	
	container = document.getElementById("player");
	containerWidth = container.parentNode.offsetWidth;
	
	spacing = containerWidth / noOfBeeps - 1; // -1 to compensate for left-border thickness
	if (spacing < 5)
	{
		spacing = 5;
	}
	
	audioFile= new Audio(fileName);
	
	var line = new Array();
	for (i = 0; i < noOfBeeps; ++i)
	{
		line[i] = document.createElement("div");
		line[i].innerHTML = "";
		line[i].setAttribute("class", "seek_line");
		line[i].style.marginLeft = (spacing / 2)+ "px";
		line[i].style.marginRight = (spacing / 2) + "px";
		line[i].setAttribute("data-timestamp", i * 5);
		container.appendChild(line[i]);
	}
	
	line[i - 1].style.marginRight = spacing + "px";
	
	for (i = 0; i < noOfBeeps; ++i)
	{
		line[i].onmouseover = function (){
			console.log(this);
			audioFile.currentTime = this.getAttribute("data-timestamp");
			console.log(audioFile.currentTime);
			audioFile.play();
			
			for (j = 0; j <= this.getAttribute("data-timestamp") / 5; ++j)
			{
				line[j].style.borderLeft = "1px solid white";
				line[j].style.boxShadow = "0px 0px 5px 1px white";
			}
			
			for (; j < noOfBeeps; ++j)
			{
				line[j].style.borderLeft = "1px solid black";
				line[j].style.boxShadow = "0px 0px 5px black";
			}
		};
	}
		
	
}