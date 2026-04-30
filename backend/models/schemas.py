from pydantic import BaseModel
from typing import List, Optional

#  REQUEST
class AnalysisRequest(BaseModel):
    code : str            
    array: Optional[str] = ""   
    size : Optional[str] = ""  
    mode : str   

#  CHART POINT
class ChartPoint(BaseModel):
    n   : int           
    time: float   

#  RESPONSE 
class AnalysisResponse(BaseModel):
    complexity : str         
    description: str          
    confidence : float       
    best       : str         
    avg        : str         
    worst      : str         
    chart_data : List[ChartPoint] = []