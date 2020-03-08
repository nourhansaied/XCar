import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: "trim"
})
export class TrimPipe implements PipeTransform { 
  transform(value: any, args?: any): any {
    if (value) return value.toString().substring(0, args - 3);
  }
}
