from pydantic import BaseModel, Field
from typing import List, Optional, Literal

# REQUEST
class AnalysisRequest(BaseModel):
    code : str
    array: Optional[str] = None
    size : Optional[str] = None
    mode : Literal["MANUAL", "AUTO"]

# CHART POINT
class ChartPoint(BaseModel):
    n   : int
    time: float

# CANDIDATE CLASS
class Candidate(BaseModel):
    name : str     
    score: float  

# RESPONSE
class AnalysisResponse(BaseModel):
    complexity       : str
    description      : str
    confidence       : float
    candidates       : List[Candidate]  = Field(default_factory=list)
    best_chart_data  : List[ChartPoint] = Field(default_factory=list)
    avg_chart_data   : List[ChartPoint] = Field(default_factory=list)
    worst_chart_data : List[ChartPoint] = Field(default_factory=list)
    best             : str
    avg              : str
    worst            : str