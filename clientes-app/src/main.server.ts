import { AppComponent } from './app/app';
import { appConfig } from './app/app.config';
import { bootstrapApplication } from '@angular/platform-browser';

const bootstrap = () => bootstrapApplication(AppComponent, appConfig);

export default bootstrap;

